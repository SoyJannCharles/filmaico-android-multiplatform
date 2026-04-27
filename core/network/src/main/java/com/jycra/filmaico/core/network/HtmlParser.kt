package com.jycra.filmaico.core.network

import org.jsoup.Jsoup
import java.net.URI

object HtmlParser {

    fun extractScripts(html: String, baseUrl: String): List<String> {

        val doc = Jsoup.parse(html, baseUrl)
        val scriptElements = doc.select("script[src]")

        val blackList = listOf(
            "cloudflare", "analytics", "ads", "pop",
            "google", "facebook", "beacon", "telemetry"
        )

        return scriptElements
            .map { it.attr("abs:src") }
            .filter { url ->
                val lowerUrl = url.lowercase()
                blackList.none { lowerUrl.contains(it) }
            }
            .reversed()
            .toList()

    }

    fun resolveTargetUrl(resolvedBase: String, originalUrl: String): String {

        return try {

            val normalizedBase = when {
                resolvedBase.startsWith("http") -> resolvedBase
                resolvedBase.startsWith("//") -> "https:$resolvedBase"
                else -> "https://$resolvedBase"
            }.trimEnd('/')

            val baseUri = URI(normalizedBase)
            val originalUri = URI(originalUrl)

            "${baseUri.scheme}://${baseUri.host}${originalUri.path}"

        } catch (e: Exception) {
            originalUrl
        }

    }

}