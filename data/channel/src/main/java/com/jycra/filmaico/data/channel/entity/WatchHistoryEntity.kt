package com.jycra.filmaico.data.channel.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "watch_history")
data class WatchHistoryEntity(
    @PrimaryKey
    val channelId: String,
    val totalWatchTimeInSeconds: Long,
    val lastWatchedTimestamp: Long
)