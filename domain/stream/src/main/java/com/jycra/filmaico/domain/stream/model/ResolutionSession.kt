package com.jycra.filmaico.domain.stream.model

data class ResolutionSession(
    val assetId: String? = null,
    val streamUrl: String? = null,
    val host: String? = null,
    val resolvedUrl: String? = null,
    val provider: String? = null
)