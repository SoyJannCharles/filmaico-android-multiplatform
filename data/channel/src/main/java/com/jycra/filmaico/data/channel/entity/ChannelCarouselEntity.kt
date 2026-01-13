package com.jycra.filmaico.data.channel.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "channel_carousels")
data class ChannelCarouselEntity(
    @PrimaryKey
    val id: String,
    val title: String,
    val order: Int,
    val queryType: String,
    val queryValueJson: String // Guardaremos el queryValue como un string JSON
)