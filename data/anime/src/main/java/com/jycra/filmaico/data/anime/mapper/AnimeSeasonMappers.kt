package com.jycra.filmaico.data.anime.mapper

import com.google.gson.Gson
import com.jycra.filmaico.core.model.anime.AnimeSeasonDto
import com.jycra.filmaico.data.anime.entity.AnimeSeasonEntity

fun AnimeSeasonDto.toEntity(gson: Gson, animeOwnerId: String): AnimeSeasonEntity {

    return AnimeSeasonEntity(
        id = id ?: "",
        name = gson.toJson(name),
        seasonNumber = seasonNumber ?: 0,
        animeOwnerId = animeOwnerId
    )

}