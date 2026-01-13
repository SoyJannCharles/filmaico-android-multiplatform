package com.jycra.filmaico.data.anime.entity

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "anime_carousels")
data class AnimeCarouselEntity(
    @PrimaryKey
    val id: String,
    val title: String,
    val order: Int,
    val queryType: String,
    val queryValueJson: String
)
