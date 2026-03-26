package com.jycra.filmaico.data.media.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.jycra.filmaico.core.model.stream.StreamDto

@Entity(
    tableName = "media",
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
data class MediaEntity(
    @PrimaryKey
    val id: String,
    val seasonId: String? = null,
    val ownerId: String? = null,
    val type: String,
    val ownerType: String,
    val name: Map<String, String>,
    val synopsis: Map<String, String> = emptyMap(),
    val imageUrl: Map<String, String> = emptyMap(),
    val isSaved: Boolean = false,
    val firstAirDate: Long? = null,
    val lastAirDate: Long? = null,
    val airDate: Long? = null,
    val status: String? = null,
    val duration: Long? = null,
    val number: Int? = null,
    val isLive: Boolean = false,
    val lastPosition: Long = 0,
    val isFinished: Boolean = false,
    val sources: List<StreamDto> = emptyList()
)