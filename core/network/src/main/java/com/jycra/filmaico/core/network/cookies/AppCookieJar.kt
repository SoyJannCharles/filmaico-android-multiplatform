package com.jycra.filmaico.core.network.cookies

import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl
import java.util.concurrent.ConcurrentHashMap
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppCookieJar @Inject constructor() : CookieJar {

    private val cookieStore = ConcurrentHashMap<String, List<Cookie>>()

    override fun loadForRequest(url: HttpUrl): List<Cookie> {
        return cookieStore[url.host] ?: emptyList()
    }

    override fun saveFromResponse(
        url: HttpUrl,
        cookies: List<Cookie>
    ) {
        cookieStore[url.host] = cookies
    }

    fun clear() {
        cookieStore.clear()
    }

}