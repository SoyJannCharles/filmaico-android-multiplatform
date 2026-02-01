package com.jycra.filmaico.data.media.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.jycra.filmaico.core.model.stream.StreamDto

@Entity(tableName = "media")
data class MediaEntity(
    @PrimaryKey
    val id: String,
    val seasonId: String? = null,
    val ownerId: String? = null,
    val type: String,
    val ownerType: String,
    val name: Map<String, String>,
    val synopsis: Map<String, String> = emptyMap(),
    val imageUrl: String,
    val isSaved: Boolean = false,
    val releaseYear: Int? = null,
    val status: String? = null,
    val duration: Long? = null,
    val order: Int? = null,
    val isLive: Boolean = false,
    val lastPosition: Long = 0,
    val isFinished: Boolean = false,
    val sources: List<StreamDto> = emptyList()
)