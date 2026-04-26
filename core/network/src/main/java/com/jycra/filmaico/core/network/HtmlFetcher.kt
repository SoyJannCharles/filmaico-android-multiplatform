package com.jycra.filmaico.core.network

import com.jycra.filmaico.core.network.di.XAuthHttpClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import javax.inject.Inject

class HtmlFetcher @Inject constructor(
    @XAuthHttpClient private val client: OkHttpClient
) {

    suspend fun fetchHtml(url: String): String? = withContext(Dispatchers.IO) {

        val request = Request.Builder()
            .url(url)
            .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36")
            .build()

        return@withContext try {
            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) return@withContext null
                response.body?.string()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }

    }

}