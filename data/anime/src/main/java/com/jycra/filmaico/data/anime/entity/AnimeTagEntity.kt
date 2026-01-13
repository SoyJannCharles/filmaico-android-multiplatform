package com.jycra.filmaico.data.anime.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "anime_tags")
data class AnimeTagEntity(
    @PrimaryKey
    val tagName: String
)