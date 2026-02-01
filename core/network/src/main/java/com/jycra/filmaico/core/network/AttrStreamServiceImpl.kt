package com.jycra.filmaico.core.network

import com.jycra.filmaico.core.model.stream.CookieDto
import com.jycra.filmaico.core.model.stream.KeysDto
import com.jycra.filmaico.core.network.api.AttrStreamApiService
import com.jycra.filmaico.data.stream.data.service.AttrStreamService
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import javax.inject.Inject

class AttrStreamServiceImpl @Inject constructor(
    private val attrStreamApiService: AttrStreamApiService
) : AttrStreamService {

    override suspend fun getJwt(url: String): Response<ResponseBody> {
        return attrStreamApiService.getJwt(url)
    }

    override suspend fun getCdnToken(
        url: String,
        authorization: String,
        origin: String,
        referer: String
    ): Response<ResponseBody> {
        return attrStreamApiService.getCdnToken(
            url = url,
            authorization = authorization,
            origin = origin,
            referer = referer
        )
    }

    override suspend fun getCookies(url: String): List<CookieDto> {
        return attrStreamApiService.getCookies(url)
    }

    override suspend fun getDrmKeys(
        url: String,
        userAgent: String,
        body: RequestBody
    ): KeysDto {
        return attrStreamApiService.getDrmKeys(url, userAgent, body)
    }

}