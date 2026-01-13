package com.jycra.filmaico.data.serie.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "serie_content",
    foreignKeys = [
        ForeignKey(
            entity = SerieSeasonEntity::class,
            parentColumns = ["id"],
            childColumns = ["seasonOwnerId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class SerieContentEntity(
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