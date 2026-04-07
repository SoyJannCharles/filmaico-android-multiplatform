package com.jycra.filmaico.domain.media.model.stream

sealed interface Stream {

    val type: StreamType
    val drmInfo: DrmInfo?
    val audio: String?
    val subtitle: String?
    val provider: String?

    data class Direct(
        val uri: String,
        override val drmInfo: DrmInfo? = null,
        val headers: Map<String, String>? = null,
        val cookieUrl: String? = null,
        override val audio: String? = null,
        override val subtitle: String? = null,
        override val provider: String? = null
    ) : Stream {
        override val type: StreamType = StreamType.DIRECT
    }

    data class WebViewScrap(
        val iframeUrl: String,
        override val drmInfo: DrmInfo? = null,
        override val audio: String? = null,
        override val subtitle: String? = null,
        override val provider: String? = null
    ) : Stream {
        override val type: StreamType = StreamType.WEBVIEW_SCRAP
    }

}