package com.jycra.filmaico.domain.stream.repository

import com.jycra.filmaico.domain.stream.model.DrmKeys

interface AttrStreamRepository {

    suspend fun getProcessedUri(uri: String, forceRefresh: Boolean = false): String

    suspend fun getCookies(url: String, forceRefresh: Boolean = false): String?

    suspend fun fetchDrmKeysFromNetwork(url: String): DrmKeys

    suspend fun getDrmKeyFromRemote(channelId: String): DrmKeys?
    suspend fun saveDrmKeyToRemote(channelId: String, keys: DrmKeys)

    suspend fun getCachedDrmKey(contentId: String): DrmKeys?
    suspend fun saveDrmKeyToCache(contentId: String, keys: DrmKeys)
    suspend fun invalidateDrmKeyCache(contentId: String)

}