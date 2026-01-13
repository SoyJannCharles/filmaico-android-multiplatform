package com.jycra.filmaico.data.stream.data.service

import com.jycra.filmaico.core.model.stream.CookieDto
import com.jycra.filmaico.core.model.stream.DrmKeysDto
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response

interface AttrStreamService {

    suspend fun getJwt(url: String): Response<ResponseBody>

    suspend fun getCdnToken(
        url: String,
        authorization: String,
        origin: String,
        referer: String
    ): Response<ResponseBody>

    suspend fun getCookies(url: String): List<CookieDto>

    suspend fun getDrmKeys(url: String, userAgent: String, body: RequestBody): DrmKeysDto

}