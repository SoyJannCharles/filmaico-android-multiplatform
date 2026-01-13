package com.jycra.filmaico.data.anime.mapper

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.jycra.filmaico.core.model.anime.AnimeDto
import com.jycra.filmaico.data.anime.entity.AnimeEntity
import com.jycra.filmaico.data.anime.entity.AnimeTagCrossRef
import com.jycra.filmaico.data.anime.entity.AnimeTagEntity
import com.jycra.filmaico.domain.anime.model.Anime
import com.jycra.filmaico.domain.stream.model.ContentStatus
import kotlin.collections.emptyList
import kotlin.collections.map

fun AnimeDto.toEntity(gson: Gson): AnimeEntity {
    return AnimeEntity(
        id = id ?: "",
        name = gson.toJson(name),
        synopsis = gson.toJson(synopsis),
        coverUrl = coverUrl ?: "",
        releaseYear = releaseYear ?: 0,
        status = status ?: ""
    )
}

fun AnimeDto.toTagEntities(): List<AnimeTagEntity> {
    return this.tags?.map { tag ->
        AnimeTagEntity(tagName = tag)
    } ?: emptyList()
}

fun AnimeDto.toCrossRefEntities(): List<AnimeTagCrossRef> {
    return this.tags?.map { tag ->
        AnimeTagCrossRef(
            animeId = id ?: "",
            tagName = tag
        )
    } ?: emptyList()
}

fun AnimeEntity.toDomain(gson: Gson): Anime {

    val localizedTextTypeToken = object : TypeToken<Map<String, String>>() {}.type

    val name = gson.fromJson<Map<String, String>>(name, localizedTextTypeToken) ?: emptyMap()
    val synopsis = gson.fromJson<Map<String, String>>(synopsis, localizedTextTypeToken) ?: emptyMap()

    return Anime(
        id = id,
        name = name,
        synopsis = synopsis,
        coverUrl = coverUrl,
        status = ContentStatus.fromValue(status),
        tags = emptyList(),
        seasons = emptyList()
    )

}