package com.jycra.filmaico.data.stream.repository

import com.jycra.filmaico.data.stream.data.dao.StreamCacheDao
import com.jycra.filmaico.data.stream.data.service.ScrapingService
import com.jycra.filmaico.data.stream.entity.StreamCacheEntity
import com.jycra.filmaico.domain.media.model.MediaType
import com.jycra.filmaico.domain.stream.repository.StreamProcessingRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StreamProcessingRepositoryImpl @Inject constructor(
    private val scrapingService: ScrapingService,
    private val streamCacheDao: StreamCacheDao
) : StreamProcessingRepository {

    override suspend fun extractWebViewUrl(pageUrl: String): Flow<String> {
        return scrapingService.extractStreamUri(pageUrl)
    }

    override suspend fun extractRegexUrl(scrapeUrl: String, regex: String, headers: Map<String, String>?): String? {
        return scrapingService.extractStreamUrl(scrapeUrl, regex, headers)
    }

    override suspend fun getCachedStream(assetId: String, mediaType: MediaType): Pair<String?, Long?> {
        val cache = streamCacheDao.getCache(assetId)
        return Pair(cache?.cachedUrl, cache?.timestamp)
    }

    override suspend fun saveStreamToCache(
        assetId: String,
        mediaType: MediaType,
        url: String?
    ) {

        if (url == null) {
            streamCacheDao.deleteCache(assetId)
            return
        }

        val existing = streamCacheDao.getCache(assetId)
        if (existing != null) {
            streamCacheDao.updateUrl(assetId, url, System.currentTimeMillis())
        } else {
            streamCacheDao.insertCache(
                StreamCacheEntity(
                    assetId = assetId,
                    cachedUrl = url,
                    cachedDrmKeys = null,
                    timestamp = System.currentTimeMillis(),
                    mediaType = mediaType.value
                )
            )
        }

    }

}