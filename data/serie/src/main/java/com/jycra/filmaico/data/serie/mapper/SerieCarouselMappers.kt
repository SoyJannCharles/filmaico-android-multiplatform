package com.jycra.filmaico.data.serie.mapper

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.jycra.filmaico.core.model.serie.SerieCarouselDto
import com.jycra.filmaico.data.serie.entity.SerieCarouselEntity
import com.jycra.filmaico.data.serie.entity.SerieEntity
import com.jycra.filmaico.domain.serie.model.SerieCarousel

fun SerieCarouselDto.toEntity(gson: Gson): SerieCarouselEntity {
    return SerieCarouselEntity(
        id = id ?: "",
        title = gson.toJson(title),
        order = order ?: 0,
        queryType = queryType ?: "tag",
        queryValueJson = gson.toJson(queryValue)
    )
}

fun SerieCarouselEntity.toDomain(gson: Gson, serieEntities: List<SerieEntity>): SerieCarousel {

    val localizedTextTypeToken = object : TypeToken<Map<String, String>>() {}.type

    val title = gson.fromJson<Map<String, String>>(title, localizedTextTypeToken) ?: emptyMap()

    return SerieCarousel(
        id = id,
        title = title,
        series = serieEntities.map { serieEntity ->
            serieEntity.toDomain(gson)
        }
    )

}