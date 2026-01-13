package com.jycra.filmaico.core.model.stream

import androidx.annotation.Keep

@Keep
data class CookieDto(
    val name: String,
    val value: String,
    val domain: String,
    val path: String,
    val secure: Boolean,
    val httpOnly: Boolean,
    val expires: Long
)