package com.jycra.filmaico.domain.stream.model

sealed interface Stream {

    val type: String
    val drmInfo: DrmInfo?

    data class Direct(
        val uri: String,
        override val drmInfo: DrmInfo? = null,
        val headers: Map<String, String>? = null,
        val cookieUrl: String? = null
    ) : Stream {
        override val type: String = "direct"
    }

    data class RegexWebView(
        val iframeUrl: String,
        override val drmInfo: DrmInfo? = null
    ) : Stream {
        override val type: String = "webview_scrap"
    }

    data class RegexScrap(
        val htmlUrl: String,
        val regexPattern: String? = null,
        override val drmInfo: DrmInfo? = null,
        val headers: Map<String, String>? = null,
        val cookieUrl: String? = null
    ) : Stream {
        override val type: String = "regex_scrap"
    }

}