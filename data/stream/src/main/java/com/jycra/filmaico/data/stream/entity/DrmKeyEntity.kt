package com.jycra.filmaico.data.stream.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "drm_keys")
data class DrmKeyEntity(
    @PrimaryKey
    val contentId: String,
    val keyJson: String,
    val cacheTimestamp: Long
)