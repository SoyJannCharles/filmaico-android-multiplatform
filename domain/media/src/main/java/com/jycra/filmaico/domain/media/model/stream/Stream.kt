package com.jycra.filmaico.domain.media.model.stream

sealed interface Stream {

    val type: StreamType
    val drmInfo: DrmInfo?

    data class Direct(
        val uri: String,
        override val drmInfo: DrmInfo? = null,
        val headers: Map<String, String>? = null,
        val cookieUrl: String? = null
    ) : Stream {
        override val type: StreamType = StreamType.DIRECT
    }

    data class WebViewScrap(
        val iframeUrl: String,
        override val drmInfo: DrmInfo? = null
    ) : Stream {
        override val type: StreamType = StreamType.WEBVIEW_SCRAP
    }

    data class RegexScrap(
        val htmlUrl: String,
        val regexPattern: String? = null,
        override val drmInfo: DrmInfo? = null,
        val headers: Map<String, String>? = null,
        val cookieUrl: String? = null
    ) : Stream {
        override val type: StreamType = StreamType.HTML_SCRAP
    }

}