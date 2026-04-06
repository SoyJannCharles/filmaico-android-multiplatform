package com.jycra.filmaico.core.network

import java.net.URI

object HtmlParser {

    fun extractVideoId(url: String): String? =
        url.substringAfterLast("/").substringBefore("?")

    fun extractScripts(html: String, baseUrl: String): List<String> {

        val regex = Regex(
            "<script[^>]+src=[\"']([^\"']+\\.js[^\"']*)[\"']",
            RegexOption.IGNORE_CASE
        )

        val uri = URI(baseUrl)

        return regex.findAll(html)
            .map { it.groupValues[1] }
            .filterNot {
                it.contains("cloudflare") || it.contains("analytics")
            }
            .map { path ->
                if (path.startsWith("/")) {
                    "${uri.scheme}://${uri.host}$path"
                } else path
            }
            .toList()

    }

    fun rebuildUrl(base: String, videoId: String?): String {
        if (videoId == null || base.contains("/e/")) return base
        return base.trimEnd('/') + "/e/$videoId"
    }

}