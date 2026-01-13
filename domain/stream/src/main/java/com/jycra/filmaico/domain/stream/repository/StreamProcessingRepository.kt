package com.jycra.filmaico.domain.stream.repository

import kotlinx.coroutines.flow.Flow

interface StreamProcessingRepository {

    suspend fun getWebViewScrapedUrl(pageUrl: String): Flow<String>
    suspend fun getRegexScrapedUrl(scrapeUrl: String, regex: String, headers: Map<String, String>?): String?

    suspend fun getCachedUrl(contentId: String, contentType: String): Pair<String?, Long?>
    suspend fun saveUrlToCache(contentId: String, contentType: String, url: String?)

}