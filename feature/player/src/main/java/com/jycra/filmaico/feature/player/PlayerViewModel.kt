package com.jycra.filmaico.feature.player

import android.content.Context
import android.graphics.Bitmap
import android.view.TextureView
import androidx.annotation.OptIn
import androidx.core.graphics.scale
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.common.Tracks
import androidx.media3.common.util.UnstableApi
import com.jycra.filmaico.core.device.Platform
import com.jycra.filmaico.core.player.PlayerManager
import com.jycra.filmaico.domain.stream.model.metadata.AudioMetadata
import com.jycra.filmaico.core.player.model.Quality
import com.jycra.filmaico.core.player.util.getAvailableQualities
import com.jycra.filmaico.core.player.util.setVideoQuality
import com.jycra.filmaico.domain.history.usecase.UpsertMediaProgressUseCase
import com.jycra.filmaico.domain.media.model.MediaType
import com.jycra.filmaico.domain.media.model.metadata.PlayerMetadata
import com.jycra.filmaico.domain.media.model.stream.Stream
import com.jycra.filmaico.domain.media.usecase.GetPlayerMetadataUseCase
import com.jycra.filmaico.domain.media.usecase.ToggleSaveStatusUseCase
import com.jycra.filmaico.domain.stream.usecase.AnalyzeProviderUseCase
import com.jycra.filmaico.domain.stream.util.StreamExtractionState
import com.jycra.filmaico.feature.player.components.settings.SettingsMenuState
import com.jycra.filmaico.shared.managers.StreamPreloadManager
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject

@HiltViewModel
class PlayerViewModel @Inject constructor(
    private val streamPreloadManager: StreamPreloadManager,
    private val getPlayerMetadataUseCase: GetPlayerMetadataUseCase,
    private val analyzeProviderUseCase: AnalyzeProviderUseCase,
    private val upsertMediaProgressUseCase: UpsertMediaProgressUseCase,
    private val toggleSaveStatusUseCase: ToggleSaveStatusUseCase,
    val playerManager: PlayerManager,
    savedStateHandle: SavedStateHandle,
    @ApplicationContext private val context: Context,
) : ViewModel() {

    private val initialMediaId: String = checkNotNull(savedStateHandle["assetId"])
    private val initialMediaType: MediaType = MediaType.fromString(checkNotNull(savedStateHandle["mediaType"]))

    private val _uiState = MutableStateFlow<PlayerUiState>(PlayerUiState.Loading())
    val uiState = _uiState.asStateFlow()

    private val _effect = Channel<PlayerUiEffect>()
    val effect = _effect.receiveAsFlow()

    val extractionState: StateFlow<StreamExtractionState> = streamPreloadManager.extractionState

    private val isPlaybackReady: Boolean
        get() = _uiState.value is PlayerUiState.Success

    var playerView: TextureView? = null

    private var rawMetadata: PlayerMetadata? = null
    private var filteredSources: List<Stream> = emptyList()
    private var currentSourceIndex = 0
    private var lastExecutionWasForced = false

    private var networkRetryCount = 0
    private val maxRetries = 3

    private var isMenuInitialized = false

    private var controlsHideJob: Job? = null
    private var progressPollerJob: Job? = null

    init {
        setupPlayerListeners()
        loadAsset(initialMediaId, initialMediaType)
    }

    fun onEvent(event: PlayerUiEvent) {
        when (event) {
            // --- Controles de Reproducción ---
            is PlayerUiEvent.OnPlayPauseToggle -> togglePlayPause()
            is PlayerUiEvent.OnNextClick -> loadNextAsset()
            is PlayerUiEvent.OnPrevClick -> loadPrevAsset()
            is PlayerUiEvent.OnSeekTo -> seekTo(event.position)
            is PlayerUiEvent.OnSeekRelative -> seekRelative(event.offset)

            is PlayerUiEvent.OnToggleSaved -> toggleSaved()

            // --- Gestión de Interfaz (Controles y Menús) ---
            is PlayerUiEvent.OnUserInteract -> {
                if (event.platform == Platform.MOBILE) {
                    toggleControls()
                } else {
                    showControlsAndResetTimer()
                }
            }
            is PlayerUiEvent.OnDismissControls -> hideControls()
            is PlayerUiEvent.OnSettingsClick -> openSettingsMenu()
            is PlayerUiEvent.OnMenuNavigate -> navigateToMenu(event.state)
            is PlayerUiEvent.OnMenuDismiss -> closeSettingsMenu()
            is PlayerUiEvent.OnQualityChange -> changeQuality(event.quality)
            is PlayerUiEvent.OnProviderChange -> changeProvider(event.provider)
            is PlayerUiEvent.OnAudioChange -> changeAudio(event.audioMetadata)

            // --- Ciclo de Vida y Navegación ---
            is PlayerUiEvent.OnPlayerReady -> onPlayerViewReady(event.playerView)
            is PlayerUiEvent.OnLifecyclePause -> pausePlayback()
            is PlayerUiEvent.OnLifecycleResume -> resumePlayback()
            is PlayerUiEvent.OnRetryPlayback -> loadAsset(initialMediaId, initialMediaType)
            is PlayerUiEvent.OnBackClick -> handleBackNavigation()
        }
    }

    private fun loadAsset(id: String, mediaType: MediaType) {

        isMenuInitialized = false

        viewModelScope.launch {

            _uiState.update { PlayerUiState.Loading("Consultando contenido...") }
            currentSourceIndex = 0

            playerManager.pause()

            val metadata = getPlayerMetadataUseCase(id, mediaType)
            rawMetadata = metadata

            if (metadata == null || metadata.sources.isEmpty()) {
                _uiState.update { PlayerUiState.Error("No se encontraron fuentes de video.") }
                return@launch
            }

            val defaultAudio = metadata.sources.first().audio
            filteredSources = metadata.sources.filter { it.audio == defaultAudio }
            currentSourceIndex = 0

            playCurrentSource(metadata)

            launch(Dispatchers.IO) {
                preloadSiblings(metadata, mediaType)
            }

        }

    }

    @OptIn(UnstableApi::class)
    private fun playCurrentSource(
        metadata: PlayerMetadata,
        startFromPosition: Long? = null,
        forceRefresh: Boolean = false
    ) {

        if (currentSourceIndex >= filteredSources.size) {
            _uiState.update { PlayerUiState.Error("No hay servidores disponibles.") }
            return
        }

        val source = filteredSources[currentSourceIndex]
        val sourceName = source.provider

        viewModelScope.launch {

            _uiState.update { PlayerUiState.Loading("Conectando con $sourceName...") }

            val observerJob = launch {
                extractionState.collect { state ->
                    if (state !is StreamExtractionState.Idle && state !is StreamExtractionState.Success) {
                        _uiState.update { PlayerUiState.Loading(state.message) }
                    }
                }
            }

            val result = streamPreloadManager.getStream(
                assetId = metadata.assetId,
                mediaType = metadata.mediaType,
                source = source,
                forceRefresh = forceRefresh
            )

            observerJob.cancel()

            result.fold(
                onSuccess = { playbackData ->

                    val player = playerManager.exoPlayer

                    val seekPos = startFromPosition ?: rawMetadata?.mediaType.let {
                        if (it != MediaType.CHANNEL) {
                            if (metadata.isFinished) 0L else metadata.lastPosition
                        } else null
                    }

                    player.stop()
                    player.clearMediaItems()

                    val mediaSource = playerManager.createMediaSource(playbackData)

                    player.setMediaSource(mediaSource)
                    player.prepare()

                    if (seekPos != null) {
                        player.seekTo(seekPos)
                    } else {
                        player.seekToDefaultPosition()
                    }

                    player.play()

                },
                onFailure = { error ->
                    playNextSource(metadata)
                }
            )

        }

    }

    private fun playNextSource(metadata: PlayerMetadata) {

        val sources = metadata.sources

        if (!lastExecutionWasForced) {
            lastExecutionWasForced = true
            playCurrentSource(metadata, forceRefresh = true)
            return
        }

        lastExecutionWasForced = false
        currentSourceIndex++

        if (currentSourceIndex < sources.size) {
            playCurrentSource(metadata)
        } else {
            _uiState.update {
                PlayerUiState.Error("No se pudo reproducir ninguna de las fuentes disponibles.")
            }
        }

    }

    private suspend fun preloadSiblings(currentMetadata: PlayerMetadata, mediaType: MediaType) {

        currentMetadata.nextContentId?.let { nextId ->
            val nextMetadata = getPlayerMetadataUseCase(nextId, mediaType)
            nextMetadata?.sources?.firstOrNull()?.let { nextSource ->
                streamPreloadManager.prefetch(nextId, mediaType, nextSource)
            }
        }

        delay(2000)

        currentMetadata.prevContentId?.let { prevId ->
            val prevMetadata = getPlayerMetadataUseCase(prevId, mediaType)
            prevMetadata?.sources?.firstOrNull()?.let { prevSource ->
                streamPreloadManager.prefetch(prevId, mediaType, prevSource)
            }
        }

    }

    private fun startProgressPoller() {

        progressPollerJob?.cancel()

        progressPollerJob = viewModelScope.launch {

            var lastSavedTime = 0L

            while (isActive) {

                val player = playerManager.exoPlayer

                if (player.isPlaying) {

                    val currentPosition = player.currentPosition
                    val bufferedPosition = player.bufferedPosition
                    val duration = player.duration

                    updatePlaybackState { state ->
                        state.copy(
                            currentPosition = currentPosition,
                            bufferedPosition = bufferedPosition
                        )
                    }

                    val currentTime = System.currentTimeMillis()
                    if (currentTime - lastSavedTime >= 10_000) {
                        saveProgress(currentPosition, duration)
                        lastSavedTime = currentTime
                    }

                }

                delay(500)

            }
        }

    }

    private fun saveProgress(currentPos: Long, duration: Long) {

        if (duration <= 0 || currentPos < 5000) return

        val meta = rawMetadata ?: return
        val isFinished = (currentPos.toFloat() / duration) > 0.90f

        viewModelScope.launch(NonCancellable) {

            val thumbnailPath = captureThumbnail()

            val progress = meta.toMediaProgress(
                thumbnailPath = thumbnailPath,
                currentPos = currentPos,
                totalDuration = duration,
                isFinished = isFinished
            )
            upsertMediaProgressUseCase(progress)

        }

    }

    private suspend fun captureThumbnail(): String? {

        try {

            val textureView = playerView ?: return null
            val assetId = rawMetadata?.assetId ?: return null

            val bitmap = withContext(Dispatchers.Main) {
                textureView.bitmap
            } ?: return null

            return withContext(Dispatchers.IO) {

                val ratio = 320f / bitmap.width
                val height = (bitmap.height * ratio).toInt()

                val resized = bitmap.scale(320, height)
                bitmap.recycle()

                val thumbnailDir = File(context.filesDir, "thumbnails")
                thumbnailDir.mkdirs()

                val file = File(thumbnailDir, "thumb_$assetId.jpg")
                FileOutputStream(file).use { out ->
                    resized.compress(Bitmap.CompressFormat.JPEG, 80, out)
                }

                resized.recycle()

                file.absolutePath

            }

        } catch (e: Exception) {
            return null
        }

    }

    private fun updatePlaybackState(transform: (PlaybackState) -> PlaybackState) {
        _uiState.update { current ->
            if (current is PlayerUiState.Success) {
                current.copy(playback = transform(current.playback))
            } else current
        }
    }

    private fun updateControlsState(transform: (ControlsState) -> ControlsState) {
        _uiState.update { current ->
            if (current is PlayerUiState.Success) {
                current.copy(controls = transform(current.controls))
            } else current
        }
    }

    // --- Controles de Reproducción ---
    private fun togglePlayPause() {

        val player = playerManager.exoPlayer

        if (player.isPlaying) {
            player.pause()
        } else {
            player.play()
        }

        showControlsAndResetTimer()

    }

    private fun loadNextAsset() {
        rawMetadata?.nextContentId?.let { nextId ->
            progressPollerJob?.cancel()
            loadAsset(nextId, initialMediaType)
        }
    }

    private fun loadPrevAsset() {
        rawMetadata?.prevContentId?.let { prevId ->
            progressPollerJob?.cancel()
            loadAsset(prevId, initialMediaType)
        }
    }

    private fun seekTo(position: Long) {

        playerManager.exoPlayer.seekTo(position)

        showControlsAndResetTimer()

    }

    private fun seekRelative(offset: Long) {

        val player = playerManager.exoPlayer

        val newPosition = (player.currentPosition + offset).coerceIn(0, player.duration)
        player.seekTo(newPosition)

        showControlsAndResetTimer()

    }

    private fun toggleSaved() {

        val meta = rawMetadata ?: return
        val videoMetadata =  (uiState.value as? PlayerUiState.Success)?.videoMetadata ?: return

        viewModelScope.launch {

            toggleSaveStatusUseCase(ownerId = meta.ownerId, isSaved = !videoMetadata.isSaved)

            _uiState.update { state ->
                if (state is PlayerUiState.Success) {
                    state.copy(
                        videoMetadata = state.videoMetadata.copy(isSaved = !videoMetadata.isSaved)
                    )
                } else state
            }

        }

    }

    // --- Gestión de Interfaz (Controles y Menús) ---
    private fun showControlsAndResetTimer() {

        controlsHideJob?.cancel()

        updateControlsState { it.copy(isVisible = true) }

        if (playerManager.exoPlayer.isPlaying) {
            controlsHideJob = viewModelScope.launch {
                delay(4000)
                updateControlsState { it.copy(isVisible = false, menuState = SettingsMenuState.NONE) }
            }
        }

    }

    private fun hideControls() {

        controlsHideJob?.cancel()

        updateControlsState {
            it.copy(
                isVisible = false,
                menuState = SettingsMenuState.NONE
            )
        }

    }

    private fun toggleControls() {

        val isCurrentlyVisible = (uiState.value as? PlayerUiState.Success)?.controls?.isVisible ?: false

        if (isCurrentlyVisible) {
            hideControls()
        } else {
            showControlsAndResetTimer()
        }

    }

    @OptIn(UnstableApi::class)
    private fun openSettingsMenu() {

        controlsHideJob?.cancel()

        _uiState.update { state ->
            if (state is PlayerUiState.Success) {

                val updatedState = if (!isMenuInitialized) {
                    initializeMenuState(state)
                } else state

                val player = playerManager.exoPlayer

                val availableQualities = player.getAvailableQualities()

                val params = player.trackSelectionParameters
                val isAutoEnabled = params.maxVideoWidth == Int.MAX_VALUE && params.overrides.isEmpty()

                val currentQuality = if (isAutoEnabled) {
                    availableQualities.find { it.isAuto }
                } else {
                    val currentMaxHeight = params.maxVideoHeight
                    availableQualities.find { !it.isAuto && it.height == currentMaxHeight }
                } ?: availableQualities.first { it.isAuto }

                updatedState.copy(
                    quality = state.quality.copy(
                        availableQualities = availableQualities,
                        currentQuality = currentQuality
                    ),
                    controls = updatedState.controls.copy(menuState = SettingsMenuState.MAIN)
                )

            } else state
        }

    }

    private fun initializeMenuState(state: PlayerUiState.Success): PlayerUiState.Success {

        val allSources = rawMetadata?.sources ?: emptyList()
        val currentStream = filteredSources.getOrNull(currentSourceIndex)

        val uniqueAudio = extractUniqueAudioMetadata(allSources)
        val currentAudio = findCurrentAudioMetadata(uniqueAudio, currentStream)

        val providersForAudio = allSources.filter { it.audio == currentAudio?.code }

        startProvidersAnalysis(providersForAudio)

        isMenuInitialized = true

        return state.copy(
            audio = state.audio.copy(
                availableAudioMetadata = uniqueAudio,
                currentAudioMetadata = currentAudio
            ),
            provider = state.provider.copy(
                availableProviders = providersForAudio,
                currentProvider = currentStream
            )
        )

    }

    private fun startProvidersAnalysis(sources: List<Stream>) {

        val assetId = rawMetadata?.assetId ?: return
        val mediaType = rawMetadata?.mediaType ?: return

        sources.forEach { source ->

            val url = when (source) {
                is Stream.Direct -> source.uri
                is Stream.WebViewScrap -> source.iframeUrl
            }

            val currentState = _uiState.value
            if (currentState is PlayerUiState.Success && currentState.provider.analysis.containsKey(url)) {
                return@forEach
            }

            viewModelScope.launch(Dispatchers.IO) {
                try {

                    val metadata = analyzeProviderUseCase(
                        assetId = assetId,
                        mediaType = mediaType,
                        source = source
                    )

                    if (metadata != null) {
                        _uiState.update { state ->
                            if (state is PlayerUiState.Success) {
                                state.copy(
                                    provider = state.provider.copy(
                                        analysis = state.provider.analysis + (url to metadata)
                                    )
                                )
                            } else state
                        }
                    }

                } catch (e: Exception) {
                }
            }
        }
    }

    private fun extractUniqueAudioMetadata(sources: List<Stream>): List<AudioMetadata> {
        return sources.map {
            AudioMetadata(code = it.audio ?: "Original", subtitleCode = it.subtitle)
        }.distinct()
    }

    private fun findCurrentAudioMetadata(
        audioList: List<AudioMetadata>,
        currentStream: Stream?
    ): AudioMetadata? {
        return audioList.find {
            it.code == (currentStream?.audio ?: "Original") && it.subtitleCode == currentStream?.subtitle
        } ?: audioList.firstOrNull()
    }

    private fun navigateToMenu(state: SettingsMenuState) {
        updateControlsState { it.copy(menuState = state) }
    }

    private fun closeSettingsMenu() {

        updateControlsState { it.copy(menuState = SettingsMenuState.NONE) }

        showControlsAndResetTimer()

    }

    private fun changeQuality(quality: Quality) {

        playerManager.exoPlayer.setVideoQuality(quality)

        _uiState.update { current ->
            if (current is PlayerUiState.Success) {
                current.copy(
                    quality = current.quality.copy(currentQuality = quality),
                    controls = current.controls.copy(menuState = SettingsMenuState.NONE)
                )
            } else current
        }

        showControlsAndResetTimer()

    }

    private fun changeProvider(provider: Stream) {

        val metadata = rawMetadata ?: return

        val index = filteredSources.indexOf(provider)

        if (index != -1) {

            currentSourceIndex = index

            val currentPos = playerManager.exoPlayer.currentPosition

            playCurrentSource(metadata, startFromPosition = currentPos)

        }

    }

    private fun changeAudio(audioMetadata: AudioMetadata) {

        val metadata = rawMetadata ?: return

        val newFilteredSources = metadata.sources.filter { it.audio == audioMetadata.code }

        if (newFilteredSources.isNotEmpty()) {

            filteredSources = newFilteredSources

            currentSourceIndex = 0

            val currentPos = playerManager.exoPlayer.currentPosition

            playCurrentSource(metadata, startFromPosition = currentPos)

        }

    }

    // --- Ciclo de Vida y Navegación ---
    private fun onPlayerViewReady(playerView: TextureView) {

        this.playerView = playerView

        if (isPlaybackReady) {
            updateControlsState { it.copy(isVisible = true) }
        }

    }

    private fun pausePlayback() {

        playerManager.pause()

        controlsHideJob?.cancel()

        updateControlsState { it.copy(isVisible = true) }

    }

    private fun resumePlayback() {

        playerManager.resume()

        showControlsAndResetTimer()

    }

    private fun handleBackNavigation() {

        _uiState.update { PlayerUiState.Closing }

        val player = playerManager.exoPlayer
        saveProgress(player.currentPosition, player.duration)

        playerManager.pause()
        viewModelScope.launch {
            _effect.send(PlayerUiEffect.NavigateBack)
        }

    }

    private fun setupPlayerListeners() {

        playerManager.exoPlayer.addListener(object : Player.Listener {

            override fun onIsPlayingChanged(isPlaying: Boolean) {

                updatePlaybackState { it.copy(isPlaying = isPlaying) }

                if (!isPlaying) {
                    controlsHideJob?.cancel()
                    updateControlsState { it.copy(isVisible = true) }
                } else {
                    showControlsAndResetTimer()
                }

            }

            override fun onPlaybackStateChanged(state: Int) {

                updatePlaybackState { it.copy(isBuffering = state == Player.STATE_BUFFERING) }

                when (state) {
                    Player.STATE_READY -> {
                        if (_uiState.value !is PlayerUiState.Success) {

                            _uiState.update {
                                PlayerUiState.Success(
                                    videoMetadata = rawMetadata!!.toVideoMetadata(),
                                    playback = PlaybackState(
                                        isPlaying = true,
                                        totalDuration = playerManager.exoPlayer.duration,
                                        isBuffering = false
                                    ),
                                    controls = ControlsState(isVisible = true),
                                    quality = QualityState(),
                                    provider = ProviderState(),
                                    audio = AudioState()
                                )
                            }

                            startProgressPoller()
                            showControlsAndResetTimer()

                        }
                    }
                    Player.STATE_ENDED -> {

                        val player = playerManager.exoPlayer
                        saveProgress(player.currentPosition, player.duration)

                        progressPollerJob?.cancel()

                        if (rawMetadata?.nextContentId != null) {
                            loadNextAsset()
                        } else {
                            handleBackNavigation()
                        }

                    }
                }

            }

            override fun onTracksChanged(tracks: Tracks) {

            }

        })

        playerManager.setPlaybackErrorCallback { error ->
            handlePlaybackError(error)
        }

    }

    private fun handlePlaybackError(error: PlaybackException) {

        val player = playerManager.exoPlayer
        val errorCode = error.errorCode

        when (errorCode) {

            PlaybackException.ERROR_CODE_IO_NETWORK_CONNECTION_FAILED,
            PlaybackException.ERROR_CODE_IO_NETWORK_CONNECTION_TIMEOUT,
            PlaybackException.ERROR_CODE_IO_UNSPECIFIED -> {

                if (networkRetryCount < maxRetries) {

                    networkRetryCount++
                    _uiState.update { PlayerUiState.Loading("Inestabilidad de red. Reintentando ($networkRetryCount/$maxRetries)...") }

                    player.prepare()
                    player.play()

                } else {
                    _uiState.update { PlayerUiState.Error("Fallo de conexión. Verifica tu internet y vuelve a intentar.") }
                }

            }

            PlaybackException.ERROR_CODE_IO_READ_POSITION_OUT_OF_RANGE -> {

                if (networkRetryCount < maxRetries) {

                    networkRetryCount++

                    _uiState.update { PlayerUiState.Loading("Sincronizando transmisión...") }

                    player.seekToDefaultPosition()
                    player.prepare()
                    player.play()

                } else {
                    _uiState.update { PlayerUiState.Error("La transmisión se interrumpió.") }
                }

            }

            PlaybackException.ERROR_CODE_BEHIND_LIVE_WINDOW -> {
                player.seekToDefaultPosition()
                player.prepare()
                player.play()
            }

            PlaybackException.ERROR_CODE_IO_BAD_HTTP_STATUS,
            PlaybackException.ERROR_CODE_IO_FILE_NOT_FOUND,
            PlaybackException.ERROR_CODE_IO_CLEARTEXT_NOT_PERMITTED -> {

                networkRetryCount = 0

                val currentPos = player.currentPosition
                val duration = player.duration
                saveProgress(currentPos, duration)

                _uiState.update { currentState ->
                    if (currentState is PlayerUiState.Success) {
                        PlayerUiState.Loading("Fuente caída. Buscando alternativa...")
                    } else currentState
                }

                rawMetadata?.let { metadata ->
                    playNextSource(metadata)
                } ?: run {
                    _uiState.update { PlayerUiState.Error("Fallo crítico: No hay más fuentes disponibles.") }
                }

            }

            PlaybackException.ERROR_CODE_DECODER_INIT_FAILED,
            PlaybackException.ERROR_CODE_DECODER_QUERY_FAILED,
            PlaybackException.ERROR_CODE_DECODING_FAILED -> {

                val currentPos = player.currentPosition
                val skipMillis = 2000L

                if (networkRetryCount < maxRetries) {

                    networkRetryCount++

                    _uiState.update {
                        PlayerUiState.Loading("Corrigiendo error de imagen... Reintentando ($networkRetryCount/$maxRetries)")
                    }

                    player.seekTo(currentPos + skipMillis)
                    player.prepare()
                    player.play()

                } else {
                    _uiState.update {
                        PlayerUiState.Error("Tu dispositivo no puede procesar este segmento del video.")
                    }
                }

            }

            else -> {
                _uiState.update { PlayerUiState.Error("Error desconocido ($errorCode).") }
            }

        }

    }

    override fun onCleared() {
        super.onCleared()
        playerManager.releasePlayer()
        controlsHideJob?.cancel()
        progressPollerJob?.cancel()
    }

}