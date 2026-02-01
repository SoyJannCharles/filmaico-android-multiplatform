package com.jycra.filmaico.core.ui.feature.media.model

data class UiMediaCarousel(
    val id: String,
    val title: String? = null,
    val hasTitle: Boolean = true,
    val items: List<UiMedia>
)