package com.jycra.filmaico.domain.stream.repository

import com.jycra.filmaico.domain.media.model.MediaType
import com.jycra.filmaico.domain.media.model.stream.DrmKeys
import kotlinx.coroutines.flow.Flow

interface PlaybackDataRepository {

    suspend fun resolveUrlViaWebView(iframeUrl: String): Flow<String>

    suspend fun getAuthenticatedUri(uri: String, forceRefresh: Boolean = false): String
    suspend fun getCookies(url: String, forceRefresh: Boolean = false): String?
    suspend fun fetchDrmKeys(url: String): DrmKeys

    suspend fun cacheStreamUrl(assetId: String, mediaType: MediaType, url: String?)
    suspend fun getCachedStreamUrl(assetId: String, mediaType: MediaType): Pair<String?, Long?>

    suspend fun preloadHlsManifest(url: String)

}