package com.jycra.filmaico.domain.stream.model.metadata

data class ProviderMetadata(
    val mainResolution: String,
    val qualityCount: Int,
    val isAdaptive: Boolean
)