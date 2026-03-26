package com.jycra.filmaico.data.media.util.mapper.dto

import com.jycra.filmaico.core.model.media.type.MovieDto
import com.jycra.filmaico.data.media.entity.MediaEntity
import com.jycra.filmaico.data.media.entity.MediaTagCrossRef
import com.jycra.filmaico.data.media.util.mapper.MediaMappingResult
import com.jycra.filmaico.domain.media.model.MediaType

fun MovieDto.toMappingResult(): MediaMappingResult {

    val movieId = this.id ?: return MediaMappingResult()

    val mediaEntity = MediaEntity(
        id = movieId,
        type = MediaType.MOVIE.value,
        ownerType = MediaType.MOVIE.value,
        seasonId = null,
        ownerId = null,
        name = this.name,
        synopsis = this.synopsis,
        imageUrl = this.posterUrl,
        airDate = this.airDate?.toDate()?.time,
        duration = this.duration,
        sources = this.sources
    )

    val tags = this.tags.map { tagName ->
        MediaTagCrossRef(mediaId = movieId, tag = tagName)
    }

    return MediaMappingResult(
        media = listOf(mediaEntity),
        tags = tags
    )

}