package com.jycra.filmaico.data.serie.mapper

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.jycra.filmaico.core.model.serie.SerieDto
import com.jycra.filmaico.data.serie.entity.SerieEntity
import com.jycra.filmaico.data.serie.entity.SerieTagCrossRef
import com.jycra.filmaico.data.serie.entity.SerieTagEntity
import com.jycra.filmaico.domain.serie.model.Serie
import com.jycra.filmaico.domain.stream.model.ContentStatus

fun SerieDto.toEntity(gson: Gson): SerieEntity {
    return SerieEntity(
        id = id ?: "",
        name = gson.toJson(name),
        synopsis = gson.toJson(synopsis),
        coverUrl = coverUrl ?: "",
        releaseYear = releaseYear ?: 0,
        status = status ?: ""
    )
}

fun SerieDto.toTagEntities(): List<SerieTagEntity> {
    return this.tags?.map { tag ->
        SerieTagEntity(tagName = tag)
    } ?: emptyList()
}

fun SerieDto.toCrossRefEntities(): List<SerieTagCrossRef> {
    return this.tags?.map { tag ->
        SerieTagCrossRef(
            serieId = id ?: "",
            tagName = tag
        )
    } ?: emptyList()
}

fun SerieEntity.toDomain(gson: Gson): Serie {

    val localizedTextTypeToken = object : TypeToken<Map<String, String>>() {}.type

    val name = gson.fromJson<Map<String, String>>(name, localizedTextTypeToken) ?: emptyMap()
    val synopsis = gson.fromJson<Map<String, String>>(synopsis, localizedTextTypeToken) ?: emptyMap()

    return Serie(
        id = id,
        name = name,
        synopsis = synopsis,
        coverUrl = coverUrl,
        releaseYear = releaseYear,
        status = ContentStatus.fromValue(status),
        tags = emptyList(),
        seasons = emptyList()
    )

}