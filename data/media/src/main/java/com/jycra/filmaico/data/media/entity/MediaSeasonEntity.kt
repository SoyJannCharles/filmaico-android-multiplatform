package com.jycra.filmaico.data.media.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "media_seasons")
data class MediaSeasonEntity(
    @PrimaryKey
    val id: String,
    val ownerId: String,
    val number: Int,
    val name: Map<String, String>
)