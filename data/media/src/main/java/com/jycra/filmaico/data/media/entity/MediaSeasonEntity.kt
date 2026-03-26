package com.jycra.filmaico.data.media.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "media_seasons",
    foreignKeys = [
        ForeignKey(
            entity = MediaEntity::class,
            parentColumns = ["id"],
            childColumns = ["ownerId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("ownerId")]
)
data class MediaSeasonEntity(
    @PrimaryKey
    val id: String,
    val ownerId: String,
    val number: Int,
    val name: Map<String, String>
)