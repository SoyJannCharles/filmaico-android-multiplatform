package com.jycra.filmaico.data.channel.mapper

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.jycra.filmaico.core.model.channel.ChannelCarouselDto
import com.jycra.filmaico.data.channel.entity.ChannelCarouselEntity
import com.jycra.filmaico.data.channel.entity.ChannelEntity
import com.jycra.filmaico.domain.channel.model.ChannelCarousel

fun ChannelCarouselDto.toEntity(gson: Gson): ChannelCarouselEntity {
    return ChannelCarouselEntity(
        id = id ?: "",
        title = gson.toJson(title),
        order = order ?: 0,
        queryType = queryType ?: "tag",
        queryValueJson = gson.toJson(this.queryValue)
    )
}

fun ChannelCarouselEntity.toDomain(gson: Gson, channelEntities: List<ChannelEntity>): ChannelCarousel {

    val localizedTextTypeToken = object : TypeToken<Map<String, String>>() {}.type

    val title = gson.fromJson<Map<String, String>>(title, localizedTextTypeToken) ?: emptyMap()

    return ChannelCarousel(
        id = id,
        title = title,
        channels = channelEntities.map { channelEntity ->
            channelEntity.toDomain(gson)
        }
    )

}