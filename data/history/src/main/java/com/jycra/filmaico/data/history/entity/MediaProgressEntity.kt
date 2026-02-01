package com.jycra.filmaico.data.history.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "media_progress")
data class MediaProgressEntity(
    @PrimaryKey
    val mediaId: String,
    val seasonId: String?,
    val ownerId: String?,
    val mediaType: String,
    val ownerMediaType: String,
    val name: Map<String, String>,
    val imageUrl: String,
    val order: Int,
    val lastPosition: Long,
    val duration: Long,
    val isFinished: Boolean,
    val lastWatchedMillis: Long
)