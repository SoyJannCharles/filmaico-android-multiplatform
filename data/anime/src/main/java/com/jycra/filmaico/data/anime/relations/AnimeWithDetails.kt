package com.jycra.filmaico.data.anime.relations

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.jycra.filmaico.data.anime.entity.AnimeEntity
import com.jycra.filmaico.data.anime.entity.AnimeSeasonEntity
import com.jycra.filmaico.data.anime.entity.AnimeTagCrossRef
import com.jycra.filmaico.data.anime.entity.AnimeTagEntity

data class AnimeWithDetails(
    @Embedded val anime: AnimeEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "tagName",
        associateBy = Junction(
            value = AnimeTagCrossRef::class,
            parentColumn = "animeId",
            entityColumn = "tagName"
        )
    )
    val tags: List<AnimeTagEntity>,
    @Relation(
        entity = AnimeSeasonEntity::class,
        parentColumn = "id",
        entityColumn = "animeOwnerId"
    )
    val seasons: List<AnimeSeasonWithContent>
)