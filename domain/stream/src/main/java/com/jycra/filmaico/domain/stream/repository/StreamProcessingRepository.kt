package com.jycra.filmaico.domain.stream.repository

import com.jycra.filmaico.domain.media.model.MediaType
import kotlinx.coroutines.flow.Flow

interface StreamProcessingRepository {

    suspend fun extractWebViewUrl(pageUrl: String): Flow<String>
    suspend fun extractRegexUrl(scrapeUrl: String, regex: String, headers: Map<String, String>?): String?

    suspend fun getCachedStream(assetId: String, mediaType: MediaType): Pair<String?, Long?>
    suspend fun saveStreamToCache(assetId: String, mediaType: MediaType, url: String?)

}