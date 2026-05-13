package com.jycra.filmaico.domain.stream.model

import com.jycra.filmaico.domain.stream.util.MediaType

data class ResolutionParams(
    val assetId: String,
    val mediaType: MediaType,
    val source: Stream,
    val allowHeavyScraping: Boolean = false,
    val forceRefresh: Boolean = false
)