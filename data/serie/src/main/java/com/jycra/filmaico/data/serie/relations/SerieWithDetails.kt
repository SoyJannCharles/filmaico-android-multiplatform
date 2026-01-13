package com.jycra.filmaico.data.serie.relations

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.jycra.filmaico.data.serie.entity.SerieEntity
import com.jycra.filmaico.data.serie.entity.SerieSeasonEntity
import com.jycra.filmaico.data.serie.entity.SerieTagCrossRef
import com.jycra.filmaico.data.serie.entity.SerieTagEntity

data class SerieWithDetails(
    @Embedded val serie: SerieEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "tagName",
        associateBy = Junction(
            value = SerieTagCrossRef::class,
            parentColumn = "serieId",
            entityColumn = "tagName"
        )
    )
    val tags: List<SerieTagEntity>,
    @Relation(
        entity = SerieSeasonEntity::class,
        parentColumn = "id",
        entityColumn = "serieOwnerId"
    )
    val seasons: List<SerieSeasonWithContent>
)