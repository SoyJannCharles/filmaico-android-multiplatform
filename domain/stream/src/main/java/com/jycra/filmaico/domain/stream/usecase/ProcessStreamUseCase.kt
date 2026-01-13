package com.jycra.filmaico.domain.stream.usecase

import com.jycra.filmaico.domain.stream.model.DrmKeys
import com.jycra.filmaico.domain.stream.model.PlaybackData
import com.jycra.filmaico.domain.stream.model.Stream
import com.jycra.filmaico.domain.stream.repository.AttrStreamRepository
import com.jycra.filmaico.domain.stream.repository.StreamProcessingRepository
import javax.inject.Inject

class ProcessStreamUseCase @Inject constructor(
    private val streamRepository: StreamProcessingRepository,
    private val attrStreamRepository: AttrStreamRepository,
    private val getStreamUrlUseCase: GetStreamUrlUsecase,
    private val getDrmKeyUseCase: GetDrmKeyUseCase,
) {

    suspend operator fun invoke(
        contentType: String,
        contentId: String,
        source: Stream,
        forceRefresh: Boolean = false,
        onReportStatus: (String) -> Unit = {}
    ): Result<PlaybackData> {
        return try {
            when (source) {
                is Stream.Direct -> {
                    processDirectStream(contentId, source, forceRefresh, onReportStatus)
                }
                is Stream.RegexWebView -> {
                    processWebViewScrapStream(contentId, contentType, source, forceRefresh)
                }
                is Stream.RegexScrap -> {
                    processRegexScrapStream(contentId, contentType, source, forceRefresh, onReportStatus)
                }
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private suspend fun processDirectStream(
        contentId: String,
        source: Stream.Direct,
        forceRefresh: Boolean,
        onReportStatus: (String) -> Unit
    ): Result<PlaybackData> {

        onReportStatus("Procesando dirección del video...")

        val uri = try {
            attrStreamRepository.getProcessedUri(source.uri, forceRefresh)
        } catch (e: Exception) {
            return Result.failure(Exception("Error al procesar URL: ${e.message}", e))
        }

        val headers = mutableMapOf<String, String>()
        source.headers?.let {
            headers.putAll(it)
        }

        source.cookieUrl?.let { url ->
            val cookie = attrStreamRepository.getCookies(url)
            if (cookie != null) {
                headers["Cookie"] = cookie
            } else {
                return Result.failure(Exception("Error al obtener la cookie persistente."))
            }
        }

        var keys: DrmKeys? = null
        source.drmInfo?.licenseUrl?.let { licenseUrl ->
            try {
                keys = getDrmKeyUseCase(contentId, licenseUrl, forceRefresh, onReportStatus)
            } catch (e: Exception) {
                return Result.failure(Exception("Error al obtener claves DRM: ${e.message}", e))
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
        contentId: String,
        contentType: String,
        source: Stream.RegexWebView,
        forceRefresh: Boolean
    ): Result<PlaybackData> {

        val streamUrl = getStreamUrlUseCase(
            contentId = contentId,
            contentType = contentType,
            pageUrl = source.iframeUrl, // Asumiendo que el campo es 'url' en RegexWebView
            forceRefresh = forceRefresh
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
        val extractedUri = streamRepository.getRegexScrapedUrl(
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