package com.jycra.filmaico.core.network.util

import com.jycra.filmaico.data.stream.util.FlowUrlResolver
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import javax.inject.Inject
import androidx.core.net.toUri
import com.jycra.filmaico.data.stream.util.toStableEdgeKey
import java.util.concurrent.ConcurrentHashMap

class FlowUrlResolverImpl @Inject constructor(
    private val client: OkHttpClient
) : FlowUrlResolver {

    companion object {
        private val ALLOWED_SUFFIXES = setOf(
            ".cvattv.com.ar", ".tvar.io", ".flow.com.ar", ".ar-cdn01.com", "mtd.llc"
        )
    }

    private val resolveCache = ConcurrentHashMap<String, String>()

    override suspend fun resolve(url: String, userAgent: String, headers: Map<String, String>): String = withContext(Dispatchers.IO) {

        val stableKey = url.toStableEdgeKey()
        resolveCache[stableKey]?.let { return@withContext it }

        val host = try {
            url.toUri().host?.lowercase()
        } catch (e: Exception) {
            null
        }

        if (host == null || !ALLOWED_SUFFIXES.any { host.endsWith(it) }) {
            return@withContext url
        }

        val requestBuilder = Request.Builder()
            .url(url)
            .get()
            .header("User-Agent", userAgent)

        headers.forEach { (key, value) ->
            if (!key.equals("User-Agent", ignoreCase = true) && !key.equals("xauthorization", ignoreCase = true)) {
                requestBuilder.header(key, value)
            }
        }

        return@withContext try {
            client.newCall(requestBuilder.build()).execute().use { response ->

                if (response.code >= 400) {
                    return@withContext url
                }

                val resolvedUrl = response.request.url.toString()

                if (resolvedUrl.contains("/tok_") && resolvedUrl.contains(".cvattv.com.ar")) {
                    resolveCache[stableKey] = resolvedUrl
                }

                resolvedUrl

            }
        } catch (e: Exception) {
            url
        }

    }

}