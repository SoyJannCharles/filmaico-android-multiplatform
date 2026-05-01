package com.jycra.filmaico.data.media.util.mapper.dto

import com.jycra.filmaico.core.firebase.model.media.MediaCarouselDto
import com.jycra.filmaico.data.media.entity.MediaCarouselEntity
import com.jycra.filmaico.domain.media.model.MediaType

fun MediaCarouselDto.toEntity(mediaType: MediaType): MediaCarouselEntity {
    return MediaCarouselEntity(
        id = this.id ?: "",
        type = mediaType.value,
        title = this.title,
        order = this.order,
        queryType = this.queryType,
        queryValue = this.queryValue?.toString() ?: ""
    )
}