package com.jycra.filmaico.data.serie.mapper

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.jycra.filmaico.core.model.serie.SerieContentDto
import com.jycra.filmaico.data.serie.entity.SerieContentEntity
import com.jycra.filmaico.domain.serie.model.SerieContent
import com.jycra.filmaico.domain.stream.model.Stream

fun SerieContentDto.toEntity(gson: Gson, seasonOwnerId: String): SerieContentEntity {
    return SerieContentEntity(
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

fun SerieContentEntity.toDomain(gson: Gson): SerieContent? {

    val sourceTypeToken = object : TypeToken<List<Stream>>() {}.type
    val sources = gson.fromJson<List<Stream>>(sourcesJson, sourceTypeToken) ?: emptyList()

    val localizedTextTypeToken = object : TypeToken<Map<String, String>>() {}.type
    val name = gson.fromJson<Map<String, String>>(name, localizedTextTypeToken) ?: emptyMap()

    return when (type) {
        "episode" -> SerieContent.Episode(
            id = id,
            order = order,
            name = name,
            duration = duration,
            thumbnailUrl = thumbnailUrl,
            sources = sources,
            episodeNumber = order
        )
        else -> null
    }

}