package com.jycra.filmaico.data.media.util.mapper.entity

import com.jycra.filmaico.data.media.entity.MediaSeasonEntity
import com.jycra.filmaico.domain.media.model.Media
import com.jycra.filmaico.domain.media.model.MediaSeason

fun MediaSeasonEntity.toDomain(assets: List<Media.Asset> = emptyList()): MediaSeason {
    return MediaSeason(
        id = this.id,
        ownerId = this.ownerId,
        number = this.number,
        name = this.name,
        episodes = assets
    )
}