package com.jycra.filmaico.data.anime.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "anime_content",
    foreignKeys = [
        ForeignKey(
            entity = AnimeSeasonEntity::class,
            parentColumns = ["id"],
            childColumns = ["seasonOwnerId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class AnimeContentEntity(
    @PrimaryKey
    val id: String,
    val type: String,
    val order: Int,
    val name: String,
    val duration: Long,
    val thumbnailUrl: String,
    val sourcesJson: String,
    val seasonOwnerId: String,
    val cachedStreamUrl: String? = null,
    val cacheTimestamp: Long? = null
)