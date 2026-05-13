package com.jycra.filmaico.domain.media.model

import com.jycra.filmaico.domain.stream.util.MediaType

data class MediaCarousel(
    val id: String,
    val title: Map<String, String>,
    val type: MediaType,
    val items: List<Media> = emptyList()
)