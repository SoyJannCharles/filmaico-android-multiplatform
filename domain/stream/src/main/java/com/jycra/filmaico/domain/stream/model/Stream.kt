package com.jycra.filmaico.domain.stream.model

import com.jycra.filmaico.domain.stream.util.StreamType

sealed interface Stream {

    val type: StreamType
    val drmContent: DrmContent?
    val audio: String?
    val subtitle: String?
    val provider: String?

    data class Direct(
        val uri: String,
        override val drmContent: DrmContent? = null,
        val headers: Map<String, String>? = null,
        val cookieUrl: String? = null,
        override val audio: String? = null,
        override val subtitle: String? = null,
        override val provider: String? = null
    ) : Stream {

        override val type: StreamType = StreamType.DIRECT

        fun isFlow(): Boolean {

            provider?.let {
                if (it.contains("flow", ignoreCase = true)) return true
            }

            val flowDomains = listOf("cvattv.com.ar", "flow.com.ar")
            return flowDomains.any { uri.contains(it, ignoreCase = true) }

        }

    }

    data class WebViewScrap(
        val iframeUrl: String,
        override val drmContent: DrmContent? = null,
        override val audio: String? = null,
        override val subtitle: String? = null,
        override val provider: String? = null
    ) : Stream {

        override val type: StreamType = StreamType.WEBVIEW_SCRAP

        fun shouldUseEvalResolver(): Boolean {
            val evalProviders = listOf("Streamwish", "Vidhide")
            return evalProviders.any { provider?.contains(it, ignoreCase = true) == true }
        }

    }

}