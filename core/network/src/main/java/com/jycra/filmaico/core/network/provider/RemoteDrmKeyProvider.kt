package com.jycra.filmaico.core.network.provider

import com.jycra.filmaico.core.firebase.model.stream.KeysDto
import com.jycra.filmaico.core.network.api.StreamCredentialsApi
import com.jycra.filmaico.data.stream.data.provider.DrmKeyProvider
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject

class RemoteDrmKeyProvider @Inject constructor(
    private val streamCredentialsApi: StreamCredentialsApi,
) : DrmKeyProvider {

    override suspend fun fetchDrmKeys(
        url: String,
        userAgent: String,
        payload: String
    ): KeysDto {

        val mediaType = "application/octet-stream".toMediaTypeOrNull()
        val requestBody = payload.toRequestBody(mediaType)

        return streamCredentialsApi.getDrmKeys(url, userAgent, requestBody)

    }

}