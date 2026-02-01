package com.jycra.filmaico.data.media.util.mapper.entity

import com.jycra.filmaico.data.media.entity.MediaCarouselEntity
import com.jycra.filmaico.domain.media.model.Media
import com.jycra.filmaico.domain.media.model.MediaCarousel
import com.jycra.filmaico.domain.media.model.MediaType

fun MediaCarouselEntity.toDomain(items: List<Media> = emptyList()): MediaCarousel {
    return MediaCarousel(
        id = this.id,
        title = this.title,
        type = MediaType.fromString(this.type),
        items = items
    )
}