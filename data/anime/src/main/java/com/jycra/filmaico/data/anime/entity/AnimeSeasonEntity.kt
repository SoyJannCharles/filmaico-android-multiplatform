package com.jycra.filmaico.data.anime.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "anime_seasons",
    foreignKeys = [
        ForeignKey(
            entity = AnimeEntity::class,
            parentColumns = ["id"],
            childColumns = ["animeOwnerId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class AnimeSeasonEntity(
    @PrimaryKey
    val id: String,
    val seasonNumber: Int,
    val name: String,
    val animeOwnerId: String
)