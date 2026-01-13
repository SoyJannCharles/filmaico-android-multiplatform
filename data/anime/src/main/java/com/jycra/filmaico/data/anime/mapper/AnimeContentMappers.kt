package com.jycra.filmaico.data.anime.mapper

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.jycra.filmaico.core.model.anime.AnimeContentDto
import com.jycra.filmaico.data.anime.entity.AnimeContentEntity
import com.jycra.filmaico.domain.anime.model.AnimeContent
import com.jycra.filmaico.domain.stream.model.Stream
import kotlin.collections.emptyList

fun AnimeContentDto.toEntity(gson: Gson, seasonOwnerId: String): AnimeContentEntity {
    return AnimeContentEntity(
        id = id ?: "",
        type = type ?: "",
        order = order ?: 0,
        name = gson.toJson(name),
        duration = duration ?: 0,
        thumbnailUrl = thumbnailUrl ?: "",
        sourcesJson = gson.toJson(sources),
        seasonOwnerId = seasonOwnerId
    )
}

fun AnimeContentEntity.toDomain(gson: Gson): AnimeContent? {

    val sourcesListType = object : TypeToken<List<Stream>>() {}.type
    val sources: List<Stream> = gson.fromJson(sourcesJson, sourcesListType) ?: emptyList()

    val localizedTextTypeToken = object : TypeToken<Map<String, String>>() {}.type
    val name = gson.fromJson<Map<String, String>>(name, localizedTextTypeToken) ?: emptyMap()

    return when (type) {
        "episode" -> AnimeContent.Episode(
            id = id,
            order = order,
            name = name,
            duration = duration,
            thumbnailUrl = thumbnailUrl,
            sources = sources,
            episodeNumber = order
        )
        "movie" -> AnimeContent.Movie(
            id = id,
            order = order,
            name = name,
            duration = duration,
            thumbnailUrl = thumbnailUrl,
            sources = sources
        )
        "ova" -> AnimeContent.Ova(
            id = id,
            order = order,
            name = name,
            duration = duration,
            thumbnailUrl = thumbnailUrl,
            sources = sources
        )
        else -> null
    }

}