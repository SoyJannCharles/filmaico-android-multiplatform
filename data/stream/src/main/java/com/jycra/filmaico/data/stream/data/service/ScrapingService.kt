package com.jycra.filmaico.data.stream.data.service

import kotlinx.coroutines.flow.Flow

interface ScrapingService {

    fun extractStreamM3u8Url(playerUrl: String): Flow<String>

    suspend fun extractStreamUrl(
        scrapeUrl: String,
        regexPattern: String,
        headers: Map<String, String>?
    ): String?

}