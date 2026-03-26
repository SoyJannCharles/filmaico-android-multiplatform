package com.jycra.filmaico.domain.media.util.mapper

import com.jycra.filmaico.domain.history.model.MediaProgress
import com.jycra.filmaico.domain.media.model.Media
import com.jycra.filmaico.domain.media.model.MediaType

fun MediaProgress.toMediaAsset(): Media.Asset {
    return Media.Asset(
        id = this.mediaId,
        name = this.name,
        imageUrl = this.imageUrl,
        tags = emptyList(),
        mediaType = MediaType.fromString(this.mediaType),
        ownerMediaType = MediaType.fromString(this.ownerMediaType),
        ownerId = this.ownerId,
        seasonId = this.seasonId,
        number = this.order,
        duration = this.duration,
        sources = emptyList(),
        lastPosition = this.lastPosition,
        isFinished = this.isFinished
    )
}