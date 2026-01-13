package com.jycra.filmaico.feature.player

import android.util.Log
import androidx.annotation.OptIn
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.C
import androidx.media3.common.util.UnstableApi
import com.jycra.filmaico.core.navigation.ContentType
import com.jycra.filmaico.core.player.PlayerManager
import com.jycra.filmaico.core.player.VideoQuality
import com.jycra.filmaico.core.reporter.ErrorReporter
import com.jycra.filmaico.domain.anime.model.localizedName
import com.jycra.filmaico.domain.anime.usecase.GetAnimeContentByIdUseCase
import com.jycra.filmaico.domain.channel.model.localizedName
import com.jycra.filmaico.domain.channel.usecase.GetChannelByIdUseCase
import com.jycra.filmaico.domain.movie.model.localizedName
import com.jycra.filmaico.domain.stream.usecase.ProcessStreamUseCase
import com.jycra.filmaico.domain.stream.model.Stream
import com.jycra.filmaico.domain.movie.usecase.GetMovieByIdUseCase
import com.jycra.filmaico.domain.serie.model.localizedName
import com.jycra.filmaico.domain.serie.usecase.GetSerieContentByIdUseCase
import com.jycra.filmaico.domain.stream.error.AllSourcesFailedException
import com.jycra.filmaico.domain.stream.usecase.InvalidateDrmKeyCacheUseCase
import com.jycra.filmaico.domain.stream.usecase.InvalidateStreamUrlCacheUseCase
import com.jycra.filmaico.feature.player.model.VideoMetadata
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlayerViewModel @Inject constructor(
    private val getMovieByIdUseCase: GetMovieByIdUseCase,
    private val getSerieContentByIdUseCase: GetSerieContentByIdUseCase,
    private val getChannelByIdUseCase: GetChannelByIdUseCase,
    private val getAnimeContentByIdUseCase: GetAnimeContentByIdUseCase,
    private val processStreamUseCase: ProcessStreamUseCase,
    private val invalidateDrmKeyCacheUseCase: InvalidateDrmKeyCacheUseCase,
    private val invalidateStreamUrlCacheUseCase: InvalidateStreamUrlCacheUseCase,
    val playerManager: PlayerManager,
    private val reporter: ErrorReporter,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val contentId: String = savedStateHandle.get<String>("contentId")!!
    private val contentType: String = savedStateHandle.get<String>("contentType")!!

    private var currentHeaderInfo: VideoMetadata? = null

    private var sources: List<Stream> = emptyList()
    private var currentSourceIndex = 0
    private var hasRetriedWithInvalidatedCache = false

    private val _uiState = MutableStateFlow<PlayerUiState>(PlayerUiState.Loading())
    val uiState = _uiState.asStateFlow()

    private val _eventChannel = Channel<PlayerUiEvent>()
    val eventFlow = _eventChannel.receiveAsFlow()

    private val _isPlayerVisible = MutableStateFlow(false)
    val isPlayerVisible = _isPlayerVisible.asStateFlow()

    private val _shouldMountPlayer = MutableStateFlow(false)
    val shouldMountPlayer = _shouldMountPlayer.asStateFlow()

    private var isPlaybackReady = false

    init {

        Log.i("PlayerVM", "Iniciando PlayerViewModel")

        playerManager.setPlaybackReadyCallback {
            isPlaybackReady = true
            val info = currentHeaderInfo ?: VideoMetadata("Desconocido", null, false)
            _uiState.value = PlayerUiState.Success(info)
            _shouldMountPlayer.value = true
            hasRetriedWithInvalidatedCache = false
        }

        playerManager.setPlaybackErrorCallback {
            handlePlaybackError()
            _isPlayerVisible.value = false
            _shouldMountPlayer.value = false
            isPlaybackReady = false
        }

        loadContentAndPlayFirstSource()

    }

    fun onPlayerViewReady() {
        Log.i("PlayerVM", "PlayerView ready")
        if (isPlaybackReady) {
            _isPlayerVisible.value = true
        }
    }

    private fun loadContentAndPlayFirstSource() {

        viewModelScope.launch {
            _uiState.value = PlayerUiState.Loading("Consultando Cache...")
            Log.i("PlayerVM", "Consultando Cache")
            _shouldMountPlayer.value = false
            isPlaybackReady = false
            val contentFound = loadContentMetadata()
            if (!contentFound || sources.isEmpty()) {
                _uiState.value = PlayerUiState.Error("No se encontraron fuentes de video.")
                _eventChannel.send(PlayerUiEvent.NavigateBackWithError("No se encontraron fuentes."))
                return@launch
            }
            playCurrentSource()
        }

    }

    private suspend fun loadContentMetadata(): Boolean {
        return when (contentType) {
            ContentType.MOVIE -> {
                val movie = getMovieByIdUseCase(contentId) ?: return false
                sources = movie.sources

                // Mapeamos a nuestro modelo de UI
                currentHeaderInfo = VideoMetadata(
                    title = movie.localizedName,
                    subtitle = null, // Películas no tienen subtítulo
                    isLive = false
                )
                true
            }
            ContentType.SERIE -> {
                val episode = getSerieContentByIdUseCase(contentId) ?: return false
                sources = episode.sources

                currentHeaderInfo = VideoMetadata(
                    title = if (episode.name.isEmpty()) "Episodio ${episode.order}" else episode.localizedName,
                    subtitle = "Episodio ${episode.order}",
                    isLive = false
                )
                true
            }
            ContentType.CHANNEL -> {
                val channel = getChannelByIdUseCase(contentId) ?: return false
                sources = channel.sources

                currentHeaderInfo = VideoMetadata(
                    title = channel.localizedName,
                    subtitle = null,
                    isLive = true // <--- Marcamos como TV en Vivo
                )
                true
            }
            ContentType.ANIME -> {
                val animeEp = getAnimeContentByIdUseCase(contentId) ?: return false
                sources = animeEp.sources

                currentHeaderInfo = VideoMetadata(
                    title = if (animeEp.name.isEmpty()) "Episodio ${animeEp.order}" else animeEp.localizedName,
                    subtitle = "Episodio ${animeEp.order}",
                    isLive = false
                )
                true
            }
            else -> false
        }
    }

    private fun playCurrentSource(forceRefresh: Boolean = false) {

        if (currentSourceIndex >= sources.size) {

            viewModelScope.launch {

                val exception = AllSourcesFailedException(
                    contentId = contentId,
                    contentType = contentType,
                    sourcesTried = sources.size
                )

                val errorContext = mapOf(
                    "content_id" to exception.contentId,
                    "content_type" to exception.contentType,
                    "total_sources_tried" to exception.sourcesTried
                )

                reporter.recordException(exception, errorContext)

                Log.e("PlayerVM", "Ninguna de las Fuentes Funciona, se informo a Crashlytics")

                _eventChannel.send(PlayerUiEvent.NavigateBackWithError("Todas nuestras fuentes fallaron para reproducir este contenido. Ya se ha generado un informe a nuestro equipo para solucionar este inconveniente."))

            }

            return

        }

        val source = sources[currentSourceIndex]

        viewModelScope.launch {

            // 2. Mensaje de carga de fuente específica
            // Muestra "Intentando fuente 1 de 4..." o "Cargando servidor: Alpha..."
            val sourceName = "Fuente #${currentSourceIndex + 1}"
            val progressMsg = "Conectando con $sourceName (${currentSourceIndex + 1}/${sources.size})"

            _uiState.value = PlayerUiState.Loading(progressMsg) // <--- AQUI

            processStreamUseCase(
                contentType,
                contentId,
                source,
                forceRefresh,
                onReportStatus = { statusMessage ->
                    // Actualizamos el estado de carga con el mensaje detallado del dominio
                    // Ejemplo: "Conectando con Fuente #1: Consultando base de datos remota..."
                    _uiState.value = PlayerUiState.Loading(
                        "Conectando con $sourceName: $statusMessage"
                    )
                }
            )
                .fold(
                    onSuccess = { playbackData ->
                        //_uiState.value = PlayerUiState.Loading("Estamos casi listos...")
                        playerManager.prepareAndPlay(playbackData)
                    },
                    onFailure = { error ->
                        Log.e("PlayerVM", "Fallo al procesar fuente. Probando la siguiente.", error)
                        playNextSource()
                    }
                )
        }

    }

    // Paso 3: Reacciona a un error de ExoPlayer
    private fun handlePlaybackError() {

        if (hasRetriedWithInvalidatedCache) {
            Log.w("PlayerVM", "El reintento con caché invalidado también falló. Probando siguiente fuente.")
            playNextSource()
        } else {
            // Es el primer fallo de reproducción para esta fuente. Invalidamos caché y reintentamos.
            Log.i("PlayerVM", "Fallo de ExoPlayer. Invalidando caché y reintentando la misma fuente.")
            hasRetriedWithInvalidatedCache = true
            viewModelScope.launch {
                _uiState.value = PlayerUiState.Loading("Error de reproducción. Limpiando caché...") // <--- AQUI
                invalidateCacheForCurrentSource()
                // Volvemos a procesar la MISMA fuente, pero forzando la recarga.
                _uiState.value = PlayerUiState.Loading("Reintentando conexión con el servidor...") // <--- AQUI
                playCurrentSource(forceRefresh = true)
            }
        }

    }

    private fun playNextSource() {
        currentSourceIndex++
        hasRetriedWithInvalidatedCache = false
        playCurrentSource()
    }

    private suspend fun invalidateCacheForCurrentSource() {
        when (contentType) {
            ContentType.CHANNEL -> invalidateDrmKeyCacheUseCase(contentId)
            ContentType.MOVIE, ContentType.SERIE, ContentType.ANIME ->
                invalidateStreamUrlCacheUseCase(contentId, contentType)
            // 'movie' no tiene lógica de caché invalidable por ahora, no se hace nada.
        }
    }

    @OptIn(UnstableApi::class)
    fun getAvailableQualities(): List<VideoQuality> {

        val player = playerManager.exoPlayer
        val videoGroups = player.currentTracks.groups.filter { it.type == C.TRACK_TYPE_VIDEO }

        val numericQualities = mutableListOf<VideoQuality>()

        // 2. Iteramos sobre los tracks disponibles para encontrar resoluciones
        videoGroups.forEach { group ->
            // Solo nos interesan los grupos soportados por el dispositivo
            if (group.isSupported) {
                for (i in 0 until group.length) {
                    val format = group.getTrackFormat(i)

                    // Filtramos tracks inválidos (a veces hay tracks sin resolución declarada)
                    if (format.height > 0) {
                        numericQualities.add(
                            VideoQuality(
                                height = format.height,
                                bitrate = format.bitrate,
                                label = "${format.height}p",
                                isAuto = false
                            )
                        )
                    }
                }
            }
        }

        // 3. Limpieza y Orden:
        // - distinctBy: A veces el m3u8 tiene varias variantes de 1080p con diferente bitrate.
        //               Para la UI simple, nos quedamos con una por resolución.
        // - sortedByDescending: Queremos que salga primero 1080p, luego 720p, etc.
        val sortedResolutions = numericQualities
            .distinctBy { it.height }
            .sortedByDescending { it.height }

        var currentHeight = player.videoSize.height

        // 3. Creamos la opción "Automático"
        val autoOption = VideoQuality(
            height = if (currentHeight != 0) currentHeight else 0,
            bitrate = 0,
            label = "Automático",
            isAuto = true
        )

        // 4. Retornamos: [Auto] + [Lista Ordenada]
        return listOf(autoOption) + sortedResolutions
    }

    fun changeVideoQuality(quality: VideoQuality) {
        val player = playerManager.exoPlayer
        val parameters = player.trackSelectionParameters.buildUpon()

        if (quality.isAuto) {
            // MODO AUTO:
            // Limpiamos cualquier restricción de resolución o bitrate.
            // Esto devuelve el control al algoritmo adaptativo de ExoPlayer.
            parameters
                .setMaxVideoBitrate(Int.MAX_VALUE)
        } else {
            // MODO MANUAL:
            // Forzamos a ExoPlayer a usar una resolución específica.
            // Al poner Min y Max iguales, lo obligamos a quedarse en ese carril.
            parameters
                .setMaxVideoSize(quality.height * 16 / 9, quality.height) // Calculamos ancho aprox o usamos max
                .setMinVideoSize(0, quality.height) // Forzamos altura mínima
                .setMaxVideoBitrate(Int.MAX_VALUE) // Dejamos bitrate libre para esa resolución

            // NOTA: Una forma más agresiva si la anterior falla es:
            // .setMaxVideoSize(Int.MAX_VALUE, quality.height)
            // .setMinVideoSize(0, quality.height)
            // ExoPlayer intentará cumplir la restricción de altura.
        }

        player.trackSelectionParameters = parameters.build()

        Log.i("PlayerVM", "Calidad cambiada a: ${quality.label}")
    }

    fun onNavigateBack() {
        onPause()
        isPlaybackReady = false
        _shouldMountPlayer.value = false
        viewModelScope.launch {
            _eventChannel.send(PlayerUiEvent.NavigateBack)
        }
    }

    fun onPause() {
        _isPlayerVisible.value = false
        playerManager.pause()
    }

    fun onResume() {
        playerManager.resume()
        if (isPlaybackReady && _shouldMountPlayer.value) {
            _isPlayerVisible.value = true
        }
    }

    override fun onCleared() {
        super.onCleared()
        isPlaybackReady = false
        _shouldMountPlayer.value = false
        playerManager.releasePlayer()
    }

}