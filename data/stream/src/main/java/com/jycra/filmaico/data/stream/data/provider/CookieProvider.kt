package com.jycra.filmaico.data.stream.data.provider

import com.jycra.filmaico.core.firebase.model.stream.CookieDto

interface CookieProvider {

    suspend fun fetchCookies(url: String): List<CookieDto>

}