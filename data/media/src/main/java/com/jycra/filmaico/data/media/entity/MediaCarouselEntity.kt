package com.jycra.filmaico.data.media.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "media_carousels")
data class MediaCarouselEntity(
    @PrimaryKey
    val id: String,
    val type: String,
    val title: Map<String, String>,
    val order: Int,
    val queryType: String,
    val queryValue: String
)