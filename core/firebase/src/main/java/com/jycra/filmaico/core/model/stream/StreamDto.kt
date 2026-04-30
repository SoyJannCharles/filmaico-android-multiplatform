package com.jycra.filmaico.core.model.stream

import androidx.annotation.Keep
import com.google.firebase.firestore.IgnoreExtraProperties

@Keep
@IgnoreExtraProperties
data class StreamDto(
    val type: String? = null,
    val uri: String? = null,
    val iframeUrl: String? = null,
    val drmInfo: DrmInfoDto? = null,
    val headers: Map<String, String>? = null,
    val cookieUrl: String? = null,
    val audio: String? = null,
    val subtitle: String? = null,
    val provider: String? = null
)