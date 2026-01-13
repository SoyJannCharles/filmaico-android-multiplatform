package com.jycra.filmaico.data.movie.mapper

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.jycra.filmaico.core.model.movie.MovieCarouselDto
import com.jycra.filmaico.data.movie.entity.MovieCarouselEntity
import com.jycra.filmaico.data.movie.entity.MovieEntity
import com.jycra.filmaico.domain.movie.model.MovieCarousel

fun MovieCarouselDto.toEntity(gson: Gson): MovieCarouselEntity {
    return MovieCarouselEntity(
        id = id ?: "",
        title = gson.toJson(title),
        order = order ?: 0,
        queryType = queryType ?: "tag",
        queryValueJson = gson.toJson(queryValue)
    )
}

fun MovieCarouselEntity.toDomain(gson: Gson, movieEntities: List<MovieEntity>): MovieCarousel {

    val localizedTextTypeToken = object : TypeToken<Map<String, String>>() {}.type

    val title = gson.fromJson<Map<String, String>>(title, localizedTextTypeToken) ?: emptyMap()

    return MovieCarousel(
        id = id,
        title = title,
        movies = movieEntities.map { movieEntity ->
            movieEntity.toDomain(gson)
        }
    )

}