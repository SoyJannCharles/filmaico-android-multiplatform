package com.jycra.filmaico.data.stream.data.provider.internal

import com.jycra.filmaico.domain.stream.model.DrmKeys

interface StreamCredentialsProvider {

    suspend fun getCookies(url: String, forceRefresh: Boolean = false): String?

    suspend fun getDrmKeys(url: String): DrmKeys

}