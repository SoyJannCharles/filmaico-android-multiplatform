package com.jycra.filmaico.feature.player.model

data class VideoMetadata(
    val title: String,
    val subtitle: String? = null,
    val isLive: Boolean = false
)