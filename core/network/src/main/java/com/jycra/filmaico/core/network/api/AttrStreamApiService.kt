package com.jycra.filmaico.core.network.api

import com.jycra.filmaico.core.model.stream.CookieDto
import com.jycra.filmaico.core.model.stream.KeysDto
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Url

interface AttrStreamApiService {

    @GET
    suspend fun getJwt(@Url url: String): Response<ResponseBody>

    @GET
    suspend fun getCdnToken(
        @Url url: String,
        @Header("Authorization") authorization: String,
        @Header("Origin") origin: String,
        @Header("Referer") referer: String
    ): Response<ResponseBody>

    @GET
    suspend fun getCookies(@Url url: String): List<CookieDto>

    @POST
    suspend fun getDrmKeys(
        @Url url: String,
        @Header("User-Agent") userAgent: String,
        @Body body: RequestBody
    ): KeysDto

}