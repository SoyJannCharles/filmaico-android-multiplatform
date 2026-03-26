package com.jycra.filmaico.core.model.stream

import androidx.annotation.Keep
import com.google.firebase.firestore.IgnoreExtraProperties

@Keep
@IgnoreExtraProperties
data class StreamDto(
    val type: String? = null,
    val uri: String? = null,
    val iframeUrl: String? = null,
    val htmlUrl: String? = null,
    val regexPattern: String? = null,
    val drmInfo: DrmInfoDto? = null,
    val headers: Map<String, String>? = null,
    val cookieUrl: String? = null
)