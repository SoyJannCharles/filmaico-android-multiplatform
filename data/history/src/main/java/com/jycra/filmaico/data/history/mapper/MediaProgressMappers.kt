package com.jycra.filmaico.data.history.mapper

import com.jycra.filmaico.data.history.entity.MediaProgressEntity
import com.jycra.filmaico.domain.history.model.MediaProgress

fun MediaProgress.toEntity(): MediaProgressEntity = MediaProgressEntity(
    mediaId = mediaId,
    seasonId = seasonId,
    ownerId = ownerId,
    mediaType = mediaType,
    ownerMediaType = ownerMediaType,
    name = name,
    imageUrl = imageUrl,
    order = order,
    lastPosition = lastPosition,
    duration = duration,
    isFinished = isFinished,
    lastWatchedMillis = lastWatchedMillis
)