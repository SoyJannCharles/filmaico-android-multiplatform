package com.jycra.filmaico.data.stream.data.provider

import com.jycra.filmaico.core.firebase.model.stream.KeysDto

interface DrmKeyProvider {

    suspend fun fetchDrmKeys(url: String, userAgent: String, payload: String): KeysDto

}