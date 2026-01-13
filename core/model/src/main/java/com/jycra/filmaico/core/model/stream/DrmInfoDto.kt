package com.jycra.filmaico.core.model.stream

import androidx.annotation.Keep

@Keep
data class DrmInfoDto(
    val scheme: String? = null,
    val licenseUrl: String? = null
)