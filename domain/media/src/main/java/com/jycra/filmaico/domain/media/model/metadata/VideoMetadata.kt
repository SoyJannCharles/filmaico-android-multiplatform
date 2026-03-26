package com.jycra.filmaico.domain.media.model.metadata

data class VideoMetadata(
    val name: Map<String, String>,
    val order: Int,
    val isLive: Boolean = false,
    val isSaved: Boolean = false
)