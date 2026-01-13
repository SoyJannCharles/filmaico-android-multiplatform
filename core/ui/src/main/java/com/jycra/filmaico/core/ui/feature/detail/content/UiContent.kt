package com.jycra.filmaico.core.ui.feature.detail.content

data class UiContent(
    val id: String,
    val type: String,
    val thumbnailUrl: String,
    val name: String,
    val duration: Long,
    val order: Int
)