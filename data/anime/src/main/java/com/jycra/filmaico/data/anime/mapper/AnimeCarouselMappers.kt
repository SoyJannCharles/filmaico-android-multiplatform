package com.jycra.filmaico.data.anime.mapper

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.jycra.filmaico.core.model.anime.AnimeCarouselDto
import com.jycra.filmaico.data.anime.entity.AnimeCarouselEntity
import com.jycra.filmaico.data.anime.entity.AnimeEntity
import com.jycra.filmaico.domain.anime.model.AnimeCarousel

fun AnimeCarouselDto.toEntity(gson: Gson): AnimeCarouselEntity {
    return AnimeCarouselEntity(
        id = id ?: "",
        title = gson.toJson(title),
        order = order ?: 0,
        queryType = queryType ?: "tag",
        queryValueJson = gson.toJson(queryValue)
    )
}

fun AnimeCarouselEntity.toDomain(gson: Gson, animeEntities: List<AnimeEntity>): AnimeCarousel {

    val localizedTextTypeToken = object : TypeToken<Map<String, String>>() {}.type

    val title = gson.fromJson<Map<String, String>>(title, localizedTextTypeToken) ?: emptyMap()

    return AnimeCarousel(
        id = id,
        title = title,
        animes = animeEntities.map { serieEntity ->
            serieEntity.toDomain(gson)
        }
    )

}