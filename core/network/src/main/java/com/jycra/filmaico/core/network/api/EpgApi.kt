package com.jycra.filmaico.core.network.api

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Streaming
import retrofit2.http.Url
import javax.inject.Singleton

@Singleton
interface EpgApi {

    @Streaming
    @GET
    suspend fun getCompressedEpg(
        @Url url: String = "https://www.open-epg.com/files/argentina.xml.gz"
    ): Response<ResponseBody>

}