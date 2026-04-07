package com.jycra.filmaico.domain.media.model.metadata

import com.jycra.filmaico.domain.media.model.stream.DrmKeys

data class PlaybackData(
    val uri: String,
    val headers: Map<String, String>? = null,
    val keys: DrmKeys? = null,
    val manifestContent: String? = null
)