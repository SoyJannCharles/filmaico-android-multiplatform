package com.jycra.filmaico.data.stream.util

import androidx.core.net.toUri

object StreamUrl {

    fun getSourceType(url: String): StreamSourceType {
        return when {
            url.contains("cvattv.com.ar") || url.contains("flow.com.ar") -> StreamSourceType.FLOW
            isScraperProvider(url) -> StreamSourceType.SEED
            else -> StreamSourceType.BASE
        }
    }

    private fun isScraperProvider(url: String) = getProviderName(url) != null

    fun getProviderName(url: String): String? {
        return when {
            url.contains("streamwish") -> "streamwish"
            url.contains("vidhide") || url.contains("earnvids") -> "vidhide"
            url.contains("filemoon") -> "filemoon"
            else -> null
        }
    }

    fun getVideoId(url: String): String? {
        if (url.contains("/e/")) {
            return url.substringAfter("/e/").substringBefore("/").substringBefore("?")
        }
        return url.trimEnd('/').split("/").lastOrNull()?.substringBefore("?")
    }

    fun toHostOnly(url: String) = try {
        url.toUri().host?.lowercase()
    } catch (e: Exception) { null }

    fun hasVideoExtension(url: String): Boolean {
        val cleanUrl = url.lowercase().substringBefore("?")
        return listOf(".m3u8", ".mp4", ".mpd", ".m3u", ".txt", ".wolf").any {
            cleanUrl.endsWith(it)
        }
    }

}