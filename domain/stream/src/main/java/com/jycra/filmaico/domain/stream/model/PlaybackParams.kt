package com.jycra.filmaico.domain.stream.model

data class PlaybackParams(
    val uri: String,
    val headers: Map<String, String>? = null,
    val keys: DrmKeys? = null
)