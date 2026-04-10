package com.jycra.filmaico.core.network

import com.jycra.filmaico.core.network.di.XAuthHttpClient
import okhttp3.OkHttpClient
import okhttp3.Request
import javax.inject.Inject

class HttpFetcher @Inject constructor(
    @XAuthHttpClient private val client: OkHttpClient
) {

    suspend fun getHtml(url: String): String? {

        val request = Request.Builder()
            .url(url)
            .header("User-Agent", "Mozilla/5.0")
            .build()

        return client.newCall(request).execute().use {
            if (!it.isSuccessful) null else it.body.string()
        }

    }

    suspend fun getJs(url: String): String? {

        val request = Request.Builder().url(url).build()

        return client.newCall(request).execute().use {
            if (!it.isSuccessful) null else it.body.string()
        }

    }

}