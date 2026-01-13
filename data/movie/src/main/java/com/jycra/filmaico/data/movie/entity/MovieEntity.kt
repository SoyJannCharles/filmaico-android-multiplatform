package com.jycra.filmaico.data.movie.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movies")
data class MovieEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val synopsis: String,
    val coverUrl: String,
    val duration: Long,
    val releaseYear: Int,
    val tagsJson: String,
    val sourcesJson: String,
    val cachedStreamUrl: String? = null,
    val cacheTimestamp: Long? = null
)