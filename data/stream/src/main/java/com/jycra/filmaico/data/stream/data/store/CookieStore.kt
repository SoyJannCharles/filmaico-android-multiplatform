package com.jycra.filmaico.data.stream.data.store

interface CookieStore {

    suspend fun getCookie(): String?

    suspend fun saveCookie(cookie: String)

    suspend fun clearCookie()

}