package com.jycra.filmaico.domain.stream.usecase

import com.jycra.filmaico.domain.media.model.MediaType
import com.jycra.filmaico.domain.stream.repository.PlaybackDataRepository
import com.jycra.filmaico.domain.stream.util.StreamExtractionState
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withTimeout
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException
import kotlin.coroutines.coroutineContext

class GetStreamUrlUseCase @Inject constructor(
    private val repository: PlaybackDataRepository
) {

    private val CACHE_DURATION_MS = 12 * 60 * 60 * 1000

    suspend operator fun invoke(
        assetId: String,
        mediaType: MediaType,
        iframeUrl: String,
        forceRefresh: Boolean = false,
        onStateChange: (StreamExtractionState) -> Unit
    ): String {

        coroutineContext.ensureActive()

        if (!forceRefresh) {

            onStateChange(StreamExtractionState.CheckingCache)
            val (cachedUrl, timestamp) = repository.getCachedStreamUrl(assetId, mediaType)

            coroutineContext.ensureActive()

            if (cachedUrl != null && timestamp != null) {

                val isCacheValid = (System.currentTimeMillis() - timestamp) < CACHE_DURATION_MS

                if (isCacheValid) {
                    onStateChange(StreamExtractionState.CacheHit)
                    return cachedUrl
                }

            }

        }

        onStateChange(StreamExtractionState.CacheBypass)
        coroutineContext.ensureActive()

        val newUrl = try {

            onStateChange(StreamExtractionState.ScrapingWebView(iframeUrl))

            withTimeout(20_000) {
                coroutineContext.ensureActive()
                repository.resolveUrlViaWebView(iframeUrl)
                    .first()
            }

        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            throw Exception("Error al analizar el sitio fuente: ${e.message}", e)
        }

        onStateChange(StreamExtractionState.SavingCache)
        repository.cacheStreamUrl(assetId, mediaType, newUrl)

        return newUrl

    }

}