package com.jycra.filmaico.data.media.util.mapper.dto

import com.jycra.filmaico.core.firebase.model.media.type.ChannelDto
import com.jycra.filmaico.data.media.entity.MediaEntity
import com.jycra.filmaico.data.media.entity.MediaTagCrossRef
import com.jycra.filmaico.data.media.util.mapper.MediaMappingResult
import com.jycra.filmaico.domain.media.model.MediaType

fun ChannelDto.toMappingResult(): MediaMappingResult {

    val channelId = this.id ?: return MediaMappingResult()

    val mediaEntity = MediaEntity(
        id = channelId,
        type = MediaType.CHANNEL.value,
        ownerType = MediaType.CHANNEL.value,
        seasonId = null,
        ownerId = null,
        name = this.name,
        imageUrl = mapOf("default" to this.iconUrl),
        epgId = epgId,
        sources = this.sources
    )

    val tags = this.tags.map { tag ->
        MediaTagCrossRef(mediaId = channelId, tag = tag)
    }

    return MediaMappingResult(
        media = listOf(mediaEntity),
        tags = tags
    )

}