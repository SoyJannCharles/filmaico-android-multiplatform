package com.jycra.filmaico.data.channel.mapper

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.jycra.filmaico.core.model.channel.ChannelDto
import com.jycra.filmaico.data.channel.entity.ChannelEntity
import com.jycra.filmaico.domain.channel.model.Channel
import com.jycra.filmaico.domain.stream.model.Stream

fun ChannelDto.toEntity(gson: Gson): ChannelEntity {
    return ChannelEntity(
        id = id ?: "",
        name = gson.toJson(name),
        iconUrl = iconUrl ?: "",
        tagsJson = gson.toJson(this.tags),
        sourcesJson = gson.toJson(this.sources)
    )
}

fun ChannelEntity.toDomain(gson: Gson): Channel {

    val tagsListType = object : TypeToken<List<String>>() {}.type
    val tags: List<String> = gson.fromJson(this.tagsJson, tagsListType) ?: emptyList()

    val sourcesListType = object : TypeToken<List<Stream>>() {}.type
    val sources: List<Stream> = gson.fromJson(this.sourcesJson, sourcesListType) ?: emptyList()

    val localizedTextTypeToken = object : TypeToken<Map<String, String>>() {}.type

    val name = gson.fromJson<Map<String, String>>(name, localizedTextTypeToken) ?: emptyMap()

    return Channel(
        id = id,
        name = name,
        iconUrl = iconUrl,
        tags = tags,
        sources = sources
    )

}