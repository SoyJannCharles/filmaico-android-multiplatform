package com.jycra.filmaico.core.network.provider

import com.jycra.filmaico.core.firebase.model.stream.CookieDto
import com.jycra.filmaico.core.network.api.StreamCredentialsApi
import com.jycra.filmaico.data.stream.data.provider.CookieProvider
import javax.inject.Inject

class RemoteCookieProvider @Inject constructor(
    private val streamCredentialsApi: StreamCredentialsApi
) : CookieProvider {

    override suspend fun fetchCookies(url: String): List<CookieDto> =
        streamCredentialsApi.getCookies(url)

}