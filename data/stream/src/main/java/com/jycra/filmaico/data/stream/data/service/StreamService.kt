package com.jycra.filmaico.data.stream.data.service

import com.jycra.filmaico.core.firebase.model.stream.CookieDto
import com.jycra.filmaico.core.firebase.model.stream.KeysDto
import kotlinx.coroutines.flow.Flow

interface StreamService {

    fun resolveUrlFromWebView(iframeUrl: String): Flow<String>

    suspend fun fetchJwtToken(url: String): String

    suspend fun fetchCdnToken(
        url: String,
        authorization: String,
        origin: String,
        referer: String
    ): String

    suspend fun fetchCookies(url: String): List<CookieDto>

    suspend fun fetchDrmKeys(url: String, userAgent: String, payload: String): KeysDto

    fun fetchHlsManifest(
        url: String,
        includeChildren: Boolean = true
    ): Flow<Triple<String, String, String>>

}