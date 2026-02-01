package com.jycra.filmaico.data.media.util.mapper.dto

import com.jycra.filmaico.core.model.media.type.SerieDto
import com.jycra.filmaico.data.media.entity.MediaEntity
import com.jycra.filmaico.data.media.entity.MediaTagCrossRef
import com.jycra.filmaico.data.media.util.mapper.MediaMappingResult
import com.jycra.filmaico.domain.media.model.MediaType

fun SerieDto.toMappingResult(): MediaMappingResult {

    val serieId = this.id ?: return MediaMappingResult()

    val mediaEntities = mutableListOf<MediaEntity>()
    val tagCrossRefs = mutableListOf<MediaTagCrossRef>()

    mediaEntities.add(
        MediaEntity(
            id = serieId,
            type = MediaType.SERIE.value,
            ownerType = MediaType.SERIE.value,
            seasonId = null,
            ownerId = null,
            name = this.name,
            synopsis = this.synopsis,
            imageUrl = this.coverUrl,
            releaseYear = this.releaseYear,
            status = this.status
        )
    )

    this.tags.forEach { tag ->
        tagCrossRefs.add(MediaTagCrossRef(mediaId = serieId, tag = tag))
    }

    return MediaMappingResult(mediaEntities, tagCrossRefs)

}