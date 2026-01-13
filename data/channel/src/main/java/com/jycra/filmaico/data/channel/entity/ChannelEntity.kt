package com.jycra.filmaico.data.channel.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "channels")
data class ChannelEntity(
    @PrimaryKey
    val id: String,
    val tagsJson: String,
    val name: String,
    val iconUrl: String,
    val sourcesJson: String
)