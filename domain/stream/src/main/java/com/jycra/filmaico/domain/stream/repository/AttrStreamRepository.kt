package com.jycra.filmaico.domain.stream.repository

import com.jycra.filmaico.domain.media.model.stream.DrmKeys

interface AttrStreamRepository {

    suspend fun getProcessedUri(uri: String, forceRefresh: Boolean = false): String

    suspend fun getCookies(url: String, forceRefresh: Boolean = false): String?

    suspend fun fetchDrmKeysFromNetwork(url: String): DrmKeys

}