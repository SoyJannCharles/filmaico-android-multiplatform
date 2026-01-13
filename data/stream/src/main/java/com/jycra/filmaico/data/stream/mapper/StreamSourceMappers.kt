package com.jycra.filmaico.data.stream.mapper

import com.jycra.filmaico.core.model.stream.DrmInfoDto
import com.jycra.filmaico.core.model.stream.StreamDto
import com.jycra.filmaico.domain.stream.model.DrmInfo
import com.jycra.filmaico.domain.stream.model.Stream

fun StreamDto.toDomain(): Stream? {
    return when (this.type) {
        "direct" -> {
            val uri = this.uri ?: return null
            Stream.Direct(
                uri = uri,
                drmInfo = this.drmInfo?.toDomain(),
                headers = this.headers,
                cookieUrl = this.cookieUrl
            )
        }
        "webview_scrap" -> {
            val url = this.iframeUrl ?: return null
            Stream.RegexWebView(
                iframeUrl = url,
                drmInfo = this.drmInfo?.toDomain()
            )
        }
        "html_scrap" -> {
            val url = this.htmlUrl ?: return null
            Stream.RegexScrap(
                htmlUrl = url,
                regexPattern = this.regexPattern,
                drmInfo = this.drmInfo?.toDomain(),
                headers = this.headers,
                cookieUrl = this.cookieUrl
            )
        }
        else -> null
    }
}

fun DrmInfoDto.toDomain(): DrmInfo? {
    val url = this.licenseUrl ?: return null
    return DrmInfo(
        scheme = this.scheme ?: "clearkey",
        licenseUrl = url
    )
}