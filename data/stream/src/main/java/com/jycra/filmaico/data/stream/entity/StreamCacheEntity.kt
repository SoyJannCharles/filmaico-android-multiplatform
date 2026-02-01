package com.jycra.filmaico.data.stream.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.jycra.filmaico.domain.media.model.stream.DrmKeys

@Entity(tableName = "stream_cache")
data class StreamCacheEntity(
    @PrimaryKey
    val assetId: String,
    val cachedUrl: String?,
    val cachedDrmKeys: DrmKeys?,
    val timestamp: Long,
    val mediaType: String
)