package com.jycra.filmaico.core.network.cookies

import com.jycra.filmaico.core.common.logger.FLog
import com.jycra.filmaico.core.common.logger.LogCategory
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
        val host = url.host
        return cookieStore[host]
            ?: cookieStore.entries.find { host.endsWith(it.key) }?.value
            ?: emptyList()
    }

    override fun saveFromResponse(
        url: HttpUrl,
        cookies: List<Cookie>
    ) {
        if (cookies.isNotEmpty()) {
            FLog.d(LogCategory.NETWORK, "Cookies stored for ${url.host}: ${cookies.size} items")
            cookieStore[url.host] = cookies
        }
    }

    fun clear() {
        cookieStore.clear()
    }

}