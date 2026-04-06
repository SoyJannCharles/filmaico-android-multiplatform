package com.jycra.filmaico.domain.stream.usecase

import com.jycra.filmaico.domain.media.model.MediaType
import com.jycra.filmaico.domain.media.model.metadata.PlaybackData
import com.jycra.filmaico.domain.media.model.stream.DrmKeys
import com.jycra.filmaico.domain.media.model.stream.Stream
import com.jycra.filmaico.domain.stream.repository.PlaybackDataRepository
import com.jycra.filmaico.domain.stream.util.StreamExtractionState
import kotlinx.coroutines.ensureActive
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException
import kotlin.coroutines.coroutineContext

class PrepareStreamUseCase @Inject constructor(
    private val repository: PlaybackDataRepository,
    private val getStreamUrlUseCase: GetStreamUrlUseCase,
    private val getDrmKeyUseCase: GetDrmKeyUseCase
) {

    suspend operator fun invoke(
        assetId: String,
        mediaType: MediaType,
        source: Stream,
        forceRefresh: Boolean = false,
        onStateChange: (StreamExtractionState) -> Unit = {}
    ): Result<PlaybackData> {

        return try {

            coroutineContext.ensureActive()

            when (source) {
                is Stream.Direct -> {
                    processDirectStream(assetId, source, forceRefresh, onStateChange)
                }
                is Stream.WebViewScrap -> {
                    processWebViewScrapStream(assetId, mediaType, source, forceRefresh, onStateChange)
                }
            }

        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            onStateChange(StreamExtractionState.Error(e.message ?: "Error desconocido", e))
            Result.failure(e)
        }

    }

    private suspend fun processDirectStream(
        assetId: String,
        source: Stream.Direct,
        forceRefresh: Boolean,
        onStateChange: (StreamExtractionState) -> Unit
    ): Result<PlaybackData> {

        coroutineContext.ensureActive()

        onStateChange(StreamExtractionState.ResolvingCDN)

        val uri = try {
            repository.getAuthenticatedUri(source.uri, forceRefresh)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            return Result.failure(Exception("Fallo al resolver CDN/Tokens: ${e.message}", e))
        }

        coroutineContext.ensureActive()
        val headers = mutableMapOf<String, String>()
        source.headers?.let {
            headers.putAll(it)
        }

        source.cookieUrl?.let { url ->

            coroutineContext.ensureActive()

            onStateChange(StreamExtractionState.FetchingCookies)

            val cookie = try {
                repository.getCookies(url, forceRefresh)
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                null
            }

            coroutineContext.ensureActive()
            if (cookie != null) {
                headers["Cookie"] = cookie
            } else {
                return Result.failure(Exception("No se pudo establecer la sesión (Cookies nulas)."))
            }

        }

        var keys: DrmKeys? = null
        source.drmInfo?.let { drmInfo ->

            coroutineContext.ensureActive()

            onStateChange(StreamExtractionState.DecryptingDRM)

            try {

                keys = getDrmKeyUseCase(
                    contentId = assetId,
                    drmInfo = drmInfo,
                    forceRefresh = forceRefresh,
                    onStateChange = onStateChange
                )

                if (keys == null && drmInfo.isValid()) {
                    throw Exception("Fallo crítico: No se obtuvieron llaves de desencriptación.")
                }

            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                return Result.failure(Exception("Error de Licencias DRM: ${e.message}", e))
            }

        }

        coroutineContext.ensureActive()
        onStateChange(StreamExtractionState.Success(uri))
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
        onStateChange: (StreamExtractionState) -> Unit
    ): Result<PlaybackData> {

        coroutineContext.ensureActive()

        val streamUrl = getStreamUrlUseCase(
            assetId = assetId,
            mediaType = mediaType,
            iframeUrl = source.iframeUrl,
            forceRefresh = forceRefresh,
            onStateChange = onStateChange
        )

        coroutineContext.ensureActive()

        val urlWithoutParams = streamUrl.substringBefore('?').lowercase()

        if (urlWithoutParams.endsWith(".m3u8") || urlWithoutParams.endsWith(".mpd")) {

            coroutineContext.ensureActive()

            try {
                onStateChange(StreamExtractionState.PreloadingManifest)
                repository.preloadHlsManifest(streamUrl)
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {

            }

        }

        coroutineContext.ensureActive()

        onStateChange(StreamExtractionState.Success(streamUrl))
        return Result.success(PlaybackData(uri = streamUrl))

    }

}