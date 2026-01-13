package com.jycra.filmaico.domain.stream.usecase

import com.jycra.filmaico.domain.stream.repository.StreamProcessingRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class GetStreamUrlUsecase @Inject constructor(
    private val streamProcessingRepository: StreamProcessingRepository
) {

    private val CACHE_DURATION_MS = 12 * 60 * 60 * 1000

    suspend operator fun invoke(
        contentId: String,
        contentType: String,
        pageUrl: String,
        forceRefresh: Boolean = false
    ): String {

        if (!forceRefresh) {
            val (cachedUrl, timestamp) = streamProcessingRepository.getCachedUrl(contentId, contentType)
            if (cachedUrl != null && timestamp != null) {
                val isCacheValid = (System.currentTimeMillis() - timestamp) < CACHE_DURATION_MS
                if (isCacheValid) {
                    return cachedUrl
                }
            }
        }

        val newUrl = streamProcessingRepository.getWebViewScrapedUrl(pageUrl).first()
        streamProcessingRepository.saveUrlToCache(contentId, contentType, newUrl)
        return newUrl

    }

}