package com.jycra.filmaico.domain.media.model

data class MediaCarousel(
    val id: String,
    val title: Map<String, String>,
    val type: MediaType,
    val items: List<Media> = emptyList()
)