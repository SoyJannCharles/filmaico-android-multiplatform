package com.jycra.filmaico.core.network.api

import com.jycra.filmaico.core.firebase.model.stream.CookieDto
import com.jycra.filmaico.core.firebase.model.stream.KeysDto
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Url

interface StreamCredentialsApi {

    @GET
    suspend fun getCookies(@Url url: String): List<CookieDto>

    @POST
    suspend fun getDrmKeys(
        @Url url: String,
        @Header("User-Agent") userAgent: String,
        @Body body: RequestBody
    ): KeysDto

}