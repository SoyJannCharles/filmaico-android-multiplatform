package com.jycra.filmaico.domain.media.model.stream

enum class StreamType(val value: String) {

    DIRECT("direct"),
    WEBVIEW_SCRAP("webview_scrap"),
    HTML_SCRAP("html_scrap");

    companion object {
        fun fromString(type: String?): StreamType? {
            return entries.find { it.value == type?.lowercase() }
        }
    }

}