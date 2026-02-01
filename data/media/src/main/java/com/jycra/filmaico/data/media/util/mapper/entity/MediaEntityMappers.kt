package com.jycra.filmaico.data.media.util.mapper.entity

import com.jycra.filmaico.data.media.entity.MediaEntity
import com.jycra.filmaico.data.media.util.mapper.dto.toDomain
import com.jycra.filmaico.domain.common.content.model.ContentStatus
import com.jycra.filmaico.domain.media.model.Media
import com.jycra.filmaico.domain.media.model.MediaSeason
import com.jycra.filmaico.domain.media.model.MediaType

fun MediaEntity.toDomain(
    tags: List<String> = emptyList(),
    lastPosition: Long = 0L,
    isFinished: Boolean = false
): Media {

    val mediaType = MediaType.fromString(this.type)

    return when (mediaType) {
        MediaType.ANIME, MediaType.SERIE -> this.toContainer(tags = tags)
        else -> this.toAsset(lastPosition, isFinished)
    }

}

fun MediaEntity.toContainer(
    tags: List<String> = emptyList(),
    seasons: List<MediaSeason> = emptyList()
): Media.Container {

    val mediaType = MediaType.fromString(this.type)
    val ownerMediaType = MediaType.fromString(this.ownerType)

    return Media.Container(
        id = this.id,
        name = this.name,
        imageUrl = this.imageUrl,
        tags = tags,
        mediaType = mediaType,
        ownerMediaType = ownerMediaType,
        isSaved = this.isSaved,
        synopsis = this.synopsis,
        releaseYear = this.releaseYear,
        status = ContentStatus.fromValue(this.status),
        seasons = seasons
    )

}

fun MediaEntity.toAsset(
    lastPosition: Long = 0L,
    isFinished: Boolean = false
): Media.Asset {

    val mediaType = MediaType.fromString(this.type)
    val ownerMediaType = MediaType.fromString(this.ownerType)

    return Media.Asset(
        id = this.id,
        name = this.name,
        imageUrl = this.imageUrl,
        tags = emptyList(),
        mediaType = mediaType,
        ownerMediaType = ownerMediaType,
        isSaved = this.isSaved,
        seasonId = this.seasonId,
        ownerId = this.ownerId,
        duration = this.duration,
        order = this.order,
        sources = this.sources.mapNotNull { it.toDomain() },
        isLive = mediaType == MediaType.CHANNEL,
        lastPosition = lastPosition,
        isFinished = isFinished
    )

}