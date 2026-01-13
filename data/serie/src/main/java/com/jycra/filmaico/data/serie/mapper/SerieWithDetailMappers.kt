package com.jycra.filmaico.data.serie.mapper

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.jycra.filmaico.data.serie.relations.SerieWithDetails
import com.jycra.filmaico.domain.serie.model.Serie
import com.jycra.filmaico.domain.serie.model.SerieContent
import com.jycra.filmaico.domain.serie.model.SerieSeason
import com.jycra.filmaico.domain.stream.model.ContentStatus
import com.jycra.filmaico.domain.stream.model.Stream

fun SerieWithDetails.toDomain(gson: Gson): Serie {

    val localizedTextTypeToken = object : TypeToken<Map<String, String>>() {}.type

    val name = gson.fromJson<Map<String, String>>(serie.name, localizedTextTypeToken) ?: emptyMap()
    val synopsis = gson.fromJson<Map<String, String>>(serie.synopsis, localizedTextTypeToken) ?: emptyMap()

    return Serie(
        id = serie.id,
        name = name,
        synopsis = synopsis,
        coverUrl = serie.coverUrl,
        releaseYear = serie.releaseYear,
        status = ContentStatus.fromValue(serie.status),
        tags = tags.map { tag -> tag.tagName },
        seasons = seasons.map { seasonWithContent ->

            val seasonName = gson.fromJson<Map<String, String>>(seasonWithContent.season.name, localizedTextTypeToken) ?: emptyMap()

            SerieSeason(
                id = seasonWithContent.season.id,
                seasonNumber = seasonWithContent.season.seasonNumber,
                name = seasonName,
                content = seasonWithContent.content.mapNotNull { contentEntity ->

                    val typeToken = object : TypeToken<List<Stream>>() {}.type
                    val sources = gson.fromJson<List<Stream>>(contentEntity.sourcesJson, typeToken) ?: emptyList()

                    val contentName = gson.fromJson<Map<String, String>>(contentEntity.name, localizedTextTypeToken) ?: emptyMap()

                    when (contentEntity.type) { // <-- CAMBIO AQUÍ
                        "episode" -> SerieContent.Episode(
                            id = contentEntity.id,
                            order = contentEntity.order,
                            name = contentName,
                            duration = contentEntity.duration,
                            thumbnailUrl = contentEntity.thumbnailUrl,
                            sources = sources,
                            episodeNumber = contentEntity.order
                        )
                        else -> null
                    }

                }
            )

        }
    )

}