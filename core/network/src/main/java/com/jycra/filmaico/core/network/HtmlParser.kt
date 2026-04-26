package com.jycra.filmaico.core.network

import org.jsoup.Jsoup
import java.net.URI

object HtmlParser {

    fun extractVideoId(url: String): String? {
        return url.substringAfterLast("/", "")
            .substringBefore("?", "")
            .ifEmpty { null }
    }

    fun extractScripts(html: String, baseUrl: String): List<String> {

        val doc = Jsoup.parse(html, baseUrl)

        val scriptElements = doc.select("script[src]")

        return scriptElements
            .map { it.attr("abs:src") }
            .filter { url ->
                val lowerUrl = url.lowercase()
                !lowerUrl.contains("cloudflare") &&
                        !lowerUrl.contains("analytics") &&
                        !lowerUrl.contains("ads")
            }
            .toList()

    }

    fun rebuildUrl(base: String, videoId: String?): String {
        if (videoId == null || base.contains("/e/")) return base
        return base.trimEnd('/') + "/e/$videoId"
    }

}