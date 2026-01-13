package com.jycra.filmaico.data.serie.mapper

import com.google.gson.Gson
import com.jycra.filmaico.core.model.serie.SerieSeasonDto
import com.jycra.filmaico.data.serie.entity.SerieSeasonEntity

fun SerieSeasonDto.toEntity(gson: Gson, serieOwnerId: String): SerieSeasonEntity {

    return SerieSeasonEntity(
        id = id ?: "",
        name = gson.toJson(name),
        seasonNumber = seasonNumber ?: 0,
        serieOwnerId = serieOwnerId
    )

}