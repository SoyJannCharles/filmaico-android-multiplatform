package com.jycra.filmaico.domain.stream.usecase

import com.jycra.filmaico.domain.media.model.MediaType
import com.jycra.filmaico.domain.media.model.metadata.PlaybackData
import com.jycra.filmaico.domain.media.model.stream.DrmKeys
import com.jycra.filmaico.domain.media.model.stream.Stream
import com.jycra.filmaico.domain.stream.repository.AttrStreamRepository
import com.jycra.filmaico.domain.stream.repository.StreamProcessingRepository
import javax.inject.Inject

class PrepareStreamUseCase @Inject constructor(
    private val streamRepository: StreamProcessingRepository,
    private val attrStreamRepository: AttrStreamRepository,
    private val getStreamUrlUseCase: GetStreamUrlUsecase,
    private val getDrmKeyUseCase: GetDrmKeyUseCase,
) {

    suspend operator fun invoke(
        assetId: String,
        mediaType: MediaType,
        source: Stream,
        forceRefresh: Boolean = false,
        onStatusUpdate: (String) -> Unit = {}
    ): Result<PlaybackData> {
        return try {
            when (source) {
                is Stream.Direct -> {
                    processDirectStream(assetId, source, forceRefresh, onStatusUpdate)
                }
                is Stream.WebViewScrap -> {
                    processWebViewScrapStream(assetId, mediaType, source, forceRefresh, onStatusUpdate)
                }
                is Stream.RegexScrap -> {
                    processRegexScrapStream(assetId, mediaType.value, source, forceRefresh, onStatusUpdate)
                }
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private suspend fun processDirectStream(
        assetId: String,
        source: Stream.Direct,
        forceRefresh: Boolean,
        onStatusUpdate: (String) -> Unit
    ): Result<PlaybackData> {

        onStatusUpdate("Estableciendo conexión segura...")

        val uri = try {
            attrStreamRepository.getProcessedUri(source.uri, forceRefresh)
        } catch (e: Exception) {
            return Result.failure(Exception("Fallo al resolver CDN/Tokens: ${e.message}", e))
        }

        val headers = mutableMapOf<String, String>()
        source.headers?.let {
            headers.putAll(it)
        }

        source.cookieUrl?.let { url ->

            onStatusUpdate("Autenticando sesión de video...")

            val cookie = attrStreamRepository.getCookies(url, forceRefresh)
            if (cookie != null) {
                headers["Cookie"] = cookie
            } else {
                return Result.failure(Exception("No se pudo establecer la sesión (Cookies nulas)."))
            }

        }

        var keys: DrmKeys? = null
        source.drmInfo?.let { drmInfo ->

            onStatusUpdate("Descifrando contenido...")

            try {

                keys = getDrmKeyUseCase(
                    contentId = assetId,
                    drmInfo = drmInfo,
                    forceRefresh = forceRefresh,
                    onStatusUpdate = onStatusUpdate
                )

                if (keys == null && drmInfo.isValid()) {
                    throw Exception("Fallo crítico: No se obtuvieron llaves de desencriptación.")
                }

            } catch (e: Exception) {
                return Result.failure(Exception("Error de Licencias DRM: ${e.message}", e))
            }

        }

        return Result.success(
            PlaybackData(
                uri = uri,
                headers = headers.ifEmpty { null },
                keys = keys
            )
        )

    }

    private suspend fun processWebViewScrapStream(
        assetId: String,
        mediaType: MediaType,
        source: Stream.WebViewScrap,
        forceRefresh: Boolean,
        onStatusUpdate: (String) -> Unit
    ): Result<PlaybackData> {

        val streamUrl = getStreamUrlUseCase(
            assetId = assetId,
            mediaType = mediaType,
            iframeUrl = source.iframeUrl,
            forceRefresh = forceRefresh,
            onStatusUpdate = onStatusUpdate
        )

        return Result.success(PlaybackData(uri = streamUrl))

    }

    private suspend fun processRegexScrapStream(
        contentId: String,
        contentType: String,
        source: Stream.RegexScrap,
        forceRefresh: Boolean,
        onReportStatus: (String) -> Unit
    ): Result<PlaybackData> {
        // 1. Extraemos la URL del stream
        val extractedUri = streamRepository.extractRegexUrl(
            source.htmlUrl,
            source.regexPattern ?: return Result.failure(Exception("Falta el patrón Regex para el scrapeo")),
            source.headers
        ) ?: return Result.failure(Exception("No se pudo extraer la URL del stream desde el HTML."))

        // 2. Una vez que tenemos la URL, la tratamos como un stream 'Direct'.
        // Creamos un objeto 'Direct' temporal para reutilizar nuestra lógica existente.
        val tempDirectSource = Stream.Direct(
            uri = extractedUri,
            drmInfo = source.drmInfo,
            headers = source.headers,
            cookieUrl = source.cookieUrl
        )

        // 3. Reutilizamos la función que ya procesa streams directos.
        return processDirectStream(contentId, tempDirectSource, forceRefresh, onReportStatus)
    }

}