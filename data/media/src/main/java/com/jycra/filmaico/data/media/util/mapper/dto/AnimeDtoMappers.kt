package com.jycra.filmaico.data.media.util.mapper.dto

import com.jycra.filmaico.core.model.media.type.AnimeDto
import com.jycra.filmaico.data.media.entity.MediaEntity
import com.jycra.filmaico.data.media.entity.MediaTagCrossRef
import com.jycra.filmaico.data.media.util.mapper.MediaMappingResult
import com.jycra.filmaico.domain.media.model.MediaType

fun AnimeDto.toMappingResult(): MediaMappingResult {

    val animeId = this.id ?: return MediaMappingResult()

    val mediaEntities = mutableListOf<MediaEntity>()
    val tagCrossRefs = mutableListOf<MediaTagCrossRef>()

    mediaEntities.add(
        MediaEntity(
            id = animeId,
            type = MediaType.ANIME.value,
            ownerType = MediaType.ANIME.value,
            seasonId = null,
            ownerId = null,
            name = this.name,
            synopsis = this.synopsis,
            imageUrl = this.posterUrl,
            firstAirDate = this.firstAirDate?.toDate()?.time,
            lastAirDate = this.lastAirDate?.toDate()?.time,
            status = this.status
        )
    )

    this.tags.forEach { tag ->
        tagCrossRefs.add(MediaTagCrossRef(mediaId = animeId, tag = tag))
    }

    return MediaMappingResult(mediaEntities, tagCrossRefs)

}