package com.jycra.filmaico.data.anime.mapper

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.jycra.filmaico.data.anime.relations.AnimeWithDetails
import com.jycra.filmaico.domain.anime.model.Anime
import com.jycra.filmaico.domain.anime.model.AnimeContent
import com.jycra.filmaico.domain.anime.model.AnimeSeason
import com.jycra.filmaico.domain.stream.model.ContentStatus
import com.jycra.filmaico.domain.stream.model.Stream

fun AnimeWithDetails.toDomain(gson: Gson): Anime {

    val localizedTextTypeToken = object : TypeToken<Map<String, String>>() {}.type

    val name = gson.fromJson<Map<String, String>>(anime.name, localizedTextTypeToken) ?: emptyMap()
    val synopsis = gson.fromJson<Map<String, String>>(anime.synopsis, localizedTextTypeToken) ?: emptyMap()

    return Anime(
        id = anime.id,
        name = name,
        synopsis = synopsis,
        coverUrl = anime.coverUrl,
        status = ContentStatus.fromValue(anime.status),
        tags = tags.map { tag -> tag.tagName },
        seasons = seasons.map { seasonWithContent ->

            val seasonName = gson.fromJson<Map<String, String>>(seasonWithContent.season.name, localizedTextTypeToken) ?: emptyMap()

            AnimeSeason(
                id = seasonWithContent.season.id,
                seasonNumber = seasonWithContent.season.seasonNumber,
                name = seasonName,
                content = seasonWithContent.content.mapNotNull { contentEntity ->

                    val sourcesListType = object : TypeToken<List<Stream>>() {}.type
                    val sources: List<Stream> = gson.fromJson(contentEntity.sourcesJson, sourcesListType) ?: emptyList()

                    val contentName = gson.fromJson<Map<String, String>>(contentEntity.name, localizedTextTypeToken) ?: emptyMap()

                    when (contentEntity.type) {
                        "episode" -> AnimeContent.Episode(
                            id = contentEntity.id,
                            order = contentEntity.order,
                            name = contentName,
                            duration = contentEntity.duration,
                            thumbnailUrl = contentEntity.thumbnailUrl,
                            sources = sources,
                            episodeNumber = contentEntity.order
                        )
                        "movie" -> AnimeContent.Movie(
                            id = contentEntity.id,
                            order = contentEntity.order,
                            name = contentName,
                            duration = contentEntity.duration,
                            thumbnailUrl = contentEntity.thumbnailUrl,
                            sources = sources
                        )
                        "ova" -> AnimeContent.Ova(
                            id = contentEntity.id,
                            order = contentEntity.order,
                            name = contentName,
                            duration = contentEntity.duration,
                            thumbnailUrl = contentEntity.thumbnailUrl,
                            sources = sources
                        )
                        else -> null
                    }

                }
            )
        }
    )

}