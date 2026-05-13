package com.jycra.filmaico.shared.managers

import com.jycra.filmaico.core.common.logger.FLog
import com.jycra.filmaico.core.common.logger.LogCategory
import com.jycra.filmaico.core.player.PlayerManager
import com.jycra.filmaico.domain.media.model.metadata.StreamMetadata
import com.jycra.filmaico.domain.media.usecase.GetStreamMetadataUseCase
import com.jycra.filmaico.domain.stream.model.ResolutionParams
import com.jycra.filmaico.domain.stream.model.ResolutionSession
import com.jycra.filmaico.domain.stream.usecase.ReportStreamFailureUseCase
import com.jycra.filmaico.domain.stream.usecase.ReportStreamSuccessUseCase
import com.jycra.filmaico.domain.stream.usecase.ResolveStreamUseCase
import com.jycra.filmaico.domain.stream.util.MediaType
import com.jycra.filmaico.domain.stream.util.PlayerErrorType
import com.jycra.filmaico.domain.stream.util.StreamExtractionState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StreamManager @Inject constructor(
    private val playerManager: PlayerManager,
    private val getStreamMetadataUseCase: GetStreamMetadataUseCase,
    private val resolveStreamUseCase: ResolveStreamUseCase,
    private val reportStreamSuccessUseCase: ReportStreamSuccessUseCase,
    private val reportStreamFailureUseCase: ReportStreamFailureUseCase
) {

    private val _extractionState = MutableStateFlow<StreamExtractionState>(StreamExtractionState.Idle)
    val extractionState: StateFlow<StreamExtractionState> = _extractionState.asStateFlow()

    private val metadataCache = mutableMapOf<String, StreamMetadata>()
    private val sessionCache = mutableMapOf<String, ResolutionSession>()

    private var currentMetadata: StreamMetadata? = null
    private var session: ResolutionSession? = null

    private var currentAssetId: String? = null
    private var currentMediaType: MediaType? = null
    private var currentSourceIndex: Int = 0

    private var networkRetryCount = 0
    private var edgeRetryCount = 0
    private val MAX_NETWORK_RETRIES = 3
    private val MAX_EDGE_RETRIES = 10

    suspend fun startStream(
        assetId: String,
        mediaType: MediaType,
        forceRefresh: Boolean = false
    ) {

        currentAssetId = assetId
        currentMediaType = mediaType

        val metadata = getMetadata(assetId, mediaType)

        val sources = metadata.sources
        if (sources.isEmpty() || currentSourceIndex >= sources.size) {
            FLog.w(LogCategory.SCRAPER, "No hay fuentes disponibles para reproducir")
            _extractionState.value = StreamExtractionState.Error("No hay fuentes disponibles para reproducir")
        }

        val selectedSource = sources[currentSourceIndex]
        try {

            val params = ResolutionParams(
                assetId = assetId,
                mediaType = mediaType,
                source = selectedSource,
                forceRefresh = forceRefresh
            )

            val result = resolveStreamUseCase(params, session, onStateChange = { state ->
                _extractionState.value = state
            })

            result.fold(
                onSuccess = { playbackData ->

                    session = playbackData.resolutionSession

                    edgeRetryCount = 0
                    networkRetryCount = 0

                    playerManager.playStream(playbackData.playbackParams)

                },
                onFailure = { error ->
                    FLog.e(
                        LogCategory.SCRAPER,
                        "Error resolviendo fuente $currentSourceIndex",
                        error
                    )
                    handleNextSource()
                }
            )

        } catch (e: Exception) {
            _extractionState.value = StreamExtractionState.Error("Error al resolver fuente", e)
        }

    }

    private fun handleNextSource() {

        currentSourceIndex++

        val assetId = currentAssetId ?: return
        val mediaType = currentMediaType ?: return

        CoroutineScope(Dispatchers.Main).launch {
            startStream(assetId, mediaType, forceRefresh = true)
        }

    }

    fun retry() {
        currentSourceIndex++
    }

    suspend fun getMetadata(assetId: String, mediaType: MediaType): StreamMetadata {

        currentMetadata?.takeIf { it.assetId == assetId }?.let {
            return it
        }

        return try {

            val metadata = getStreamMetadataUseCase(assetId, mediaType)
                ?: throw IllegalStateException("No se pudo obtener metadata para el asset: $assetId")

            currentMetadata = metadata
            metadata

        } catch (e: Exception) {
            throw Exception("Error al cargar metadata", e)
        }

    }

    suspend fun reportSuccess() {
        val session = sessionCache[currentAssetId]

        if (session == null) {
            return
        }

        reportStreamSuccessUseCase(session.copy(assetId = currentAssetId))
    }

    suspend fun reportFailure(reason: String) {
        val session = sessionCache[currentAssetId]

        if (session == null) {
            return
        }

        FLog.w(LogCategory.NETWORK, "Reporting Failure: $reason")
        reportStreamFailureUseCase(session.copy(assetId = currentAssetId), reason)
    }

    fun setupPlayerListener(
        onIsPlayingChanged: (isPlaying: Boolean) -> Unit,
        onPlaybackStateChanged: (state: Int) -> Unit
    ) {

        playerManager.setupListeners(
            onIsPlayingChanged = onIsPlayingChanged,
            onPlaybackStateChanged = onPlaybackStateChanged,
            onPlayerError = { type, retry, seekToDefault, nextSource ->

                val scope = CoroutineScope(Dispatchers.Main)

                when (type) {
                    PlayerErrorType.BehindLiveWindow -> seekToDefault()
                    PlayerErrorType.ReadPosition -> {
                        if (networkRetryCount < MAX_NETWORK_RETRIES) {
                            networkRetryCount++
                            _extractionState.value = StreamExtractionState.Retrying(networkRetryCount, MAX_NETWORK_RETRIES, "Sincronizando...")
                            seekToDefault()
                        } else {
                            _extractionState.value = StreamExtractionState.PlayerError.FileCorrupt
                        }
                    }
                    PlayerErrorType.NetworkInstability -> {
                        if (networkRetryCount < MAX_NETWORK_RETRIES) {
                            networkRetryCount++
                            _extractionState.value = StreamExtractionState.Retrying(networkRetryCount, MAX_NETWORK_RETRIES, "Inestabilidad de red")
                            retry()
                        } else {
                            _extractionState.value = StreamExtractionState.PlayerError.NetworkFailure
                        }
                    }
                    PlayerErrorType.Decoding -> {

                        val currentPosition = playerManager.currentPosition()
                        if (networkRetryCount < MAX_NETWORK_RETRIES) {
                            networkRetryCount++
                            _extractionState.value = StreamExtractionState.Retrying(networkRetryCount, MAX_NETWORK_RETRIES, "Corrigiendo video...")
                            playerManager.seekTo(currentPosition + 2000L)
                        } else {
                            _extractionState.value = StreamExtractionState.PlayerError.DecodingIssue
                        }

                    }
                    PlayerErrorType.EdgeDisconnection -> {

                        if (edgeRetryCount < MAX_EDGE_RETRIES) {
                            edgeRetryCount++
                            scope.launch {
                                // Llamamos a startStream con mismos params y forceRefresh
                                currentAssetId?.let { assetId ->
                                    currentMediaType?.let { type ->
                                        startStream(assetId, type, forceRefresh = true)
                                    }
                                }
                            }
                        } else {
                            handleNextSource()
                        }

                    }
                    PlayerErrorType.SourceFailed -> {
                        handleNextSource() // Esto esta bien
                    }
                    is PlayerErrorType.Unknown -> {
                        // Actualizamos el estado para que la ui marque el error y el codigo.
                        FLog.w(LogCategory.PLAYER, "Reporting Failure: ${type.code}")
                        _extractionState.value = StreamExtractionState.PlayerError.Critical(type.code)
                    }
                }
            }
        )

    }

    fun clear() {

        currentSourceIndex = 0
        metadataCache.clear()
        sessionCache.clear()

        playerManager.releasePlayer()

    }

}