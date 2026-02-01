package com.jycra.filmaico.domain.stream.usecase

import com.jycra.filmaico.domain.media.model.MediaType
import com.jycra.filmaico.domain.stream.repository.StreamProcessingRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withTimeout
import javax.inject.Inject

class GetStreamUrlUsecase @Inject constructor(
    private val streamProcessingRepository: StreamProcessingRepository
) {

    private val CACHE_DURATION_MS = 12 * 60 * 60 * 1000

    suspend operator fun invoke(
        assetId: String,
        mediaType: MediaType,
        iframeUrl: String,
        forceRefresh: Boolean = false,
        onStatusUpdate: (String) -> Unit
    ): String {

        if (!forceRefresh) {

            onStatusUpdate("Verificando acceso rápido...")
            val (cachedUrl, timestamp) = streamProcessingRepository.getCachedStream(assetId, mediaType)
            if (cachedUrl != null && timestamp != null) {

                val isCacheValid = (System.currentTimeMillis() - timestamp) < CACHE_DURATION_MS

                if (isCacheValid) {
                    return cachedUrl
                }

            }

        }

        onStatusUpdate("Conectando con la fuente original...")

        val newUrl = try {
            withTimeout(20_000) {
                streamProcessingRepository.extractWebViewUrl(iframeUrl).first()
            }
        } catch (e: Exception) {
            throw Exception("Error al analizar el sitio fuente: ${e.message}", e)
        }

        onStatusUpdate("Optimizando reproducción...")
        streamProcessingRepository.saveStreamToCache(assetId, mediaType, newUrl)
        return newUrl

    }

}