package com.jycra.filmaico.data.movie.mapper

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.jycra.filmaico.core.model.movie.MovieDto
import com.jycra.filmaico.core.model.stream.StreamDto
import com.jycra.filmaico.data.movie.entity.MovieEntity
import com.jycra.filmaico.domain.movie.model.Movie
import com.jycra.filmaico.domain.stream.model.Stream

fun MovieDto.toEntity(gson: Gson): MovieEntity {
    return MovieEntity(
        id = id ?: "",
        name = gson.toJson(name),
        synopsis = gson.toJson(synopsis),
        coverUrl = coverUrl ?: "",
        duration = duration ?: 0,
        releaseYear = releaseYear ?: 0,
        tagsJson = gson.toJson(tags ?: emptyList<String>()),
        sourcesJson = gson.toJson(sources ?: emptyList<StreamDto>())
    )
}

fun MovieEntity.toDomain(gson: Gson): Movie {

    val tagsListType = object : TypeToken<List<String>>() {}.type
    val tags: List<String> = gson.fromJson(this.tagsJson, tagsListType) ?: emptyList()

    val sourcesListType = object : TypeToken<List<Stream>>() {}.type
    val sources: List<Stream> = gson.fromJson(this.sourcesJson, sourcesListType) ?: emptyList()

    val localizedTextTypeToken = object : TypeToken<Map<String, String>>() {}.type

    val name = gson.fromJson<Map<String, String>>(name, localizedTextTypeToken) ?: emptyMap()
    val synopsis = gson.fromJson<Map<String, String>>(synopsis, localizedTextTypeToken) ?: emptyMap()

    return Movie(
        id = id,
        name = name,
        synopsis = synopsis,
        coverUrl = coverUrl,
        duration = duration,
        releaseYear = releaseYear,
        tags = tags,
        sources = sources
    )

}