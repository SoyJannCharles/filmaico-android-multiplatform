package com.jycra.filmaico.data.anime.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.jycra.filmaico.data.anime.entity.AnimeContentEntity
import com.jycra.filmaico.data.anime.entity.AnimeSeasonEntity

data class AnimeSeasonWithContent(
    @Embedded
    val season: AnimeSeasonEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "seasonOwnerId"
    )
    val content: List<AnimeContentEntity>
)