package com.jycra.filmaico.data.movie.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movie_carousels")
data class MovieCarouselEntity(
    @PrimaryKey
    val id: String,
    val title: String,
    val order: Int,
    val queryType: String,
    val queryValueJson: String
)