package com.jycra.filmaico.data.media.util.mapper.entity

import com.jycra.filmaico.data.media.entity.EpgEntity
import com.jycra.filmaico.domain.media.model.Epg

fun EpgEntity.toDomain(): Epg {
    return Epg(
        epgId = this.epgId,
        title = this.title,
        description = this.description,
        startTime = this.startTime,
        endTime = this.endTime
    )
}