package com.jycra.filmaico.data.serie.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.jycra.filmaico.data.serie.entity.SerieContentEntity
import com.jycra.filmaico.data.serie.entity.SerieSeasonEntity

data class SerieSeasonWithContent(
    @Embedded
    val season: SerieSeasonEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "seasonOwnerId"
    )
    val content: List<SerieContentEntity>
)