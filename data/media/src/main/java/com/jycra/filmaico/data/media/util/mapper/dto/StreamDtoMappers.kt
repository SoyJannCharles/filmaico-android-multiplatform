package com.jycra.filmaico.data.media.util.mapper.dto

import com.jycra.filmaico.core.model.stream.DrmInfoDto
import com.jycra.filmaico.core.model.stream.StreamDto
import com.jycra.filmaico.domain.media.model.stream.DrmInfo
import com.jycra.filmaico.domain.media.model.stream.Key
import com.jycra.filmaico.domain.media.model.stream.Stream
import com.jycra.filmaico.domain.media.model.stream.StreamType

fun StreamDto.toDomain(): Stream? {
    return when (StreamType.fromString(this.type)) {
        StreamType.DIRECT -> {
            val uri = this.uri ?: return null
            Stream.Direct(
                uri = uri,
                drmInfo = this.drmInfo?.toDomain(),
                headers = this.headers,
                cookieUrl = this.cookieUrl
            )
        }
        StreamType.WEBVIEW_SCRAP -> {
            val url = this.iframeUrl ?: return null
            Stream.WebViewScrap(
                iframeUrl = url,
                drmInfo = this.drmInfo?.toDomain()
            )
        }
        StreamType.HTML_SCRAP -> {
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

    if (!this.isValid()) return null

    return DrmInfo(
        scheme = this.scheme,
        licenseUrl = this.licenseUrl,
        staticKeys = Key(
            kty = this.staticKeys.kty,
            k = this.staticKeys.k,
            kid = this.staticKeys.kid
        )
    )

}