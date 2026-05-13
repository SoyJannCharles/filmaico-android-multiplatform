package com.jycra.filmaico.data.media.util.mapper.dto

import com.jycra.filmaico.core.firebase.model.stream.DrmInfoDto
import com.jycra.filmaico.core.firebase.model.stream.StreamDto
import com.jycra.filmaico.domain.stream.model.DrmContent
import com.jycra.filmaico.domain.stream.model.Key
import com.jycra.filmaico.domain.stream.model.Stream
import com.jycra.filmaico.domain.stream.util.StreamType

fun StreamDto.toDomain(): Stream? {
    return when (StreamType.fromString(this.type)) {
        StreamType.DIRECT -> {
            val uri = this.uri ?: return null
            Stream.Direct(
                uri = uri,
                drmContent = this.drmInfo?.toDomain(),
                headers = this.headers,
                cookieUrl = this.cookieUrl,
                audio = this.audio,
                subtitle = this.subtitle,
                provider = this.provider
            )
        }
        StreamType.WEBVIEW_SCRAP -> {
            val url = this.iframeUrl ?: return null
            Stream.WebViewScrap(
                iframeUrl = url,
                drmContent = this.drmInfo?.toDomain(),
                audio = this.audio,
                subtitle = this.subtitle,
                provider = this.provider
            )
        }
        else -> null
    }
}

fun DrmInfoDto.toDomain(): DrmContent? {

    if (!this.isValid()) return null

    return DrmContent(
        scheme = this.scheme,
        licenseUrl = this.licenseUrl,
        staticKeys = Key(
            kty = this.staticKeys.kty,
            k = this.staticKeys.k,
            kid = this.staticKeys.kid
        )
    )

}