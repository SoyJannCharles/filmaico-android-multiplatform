package com.jycra.filmaico.data.media.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "epg")
data class EpgEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val epgId: String,
    val title: String,
    val description: String?,
    val startTime: Long,
    val endTime: Long
)