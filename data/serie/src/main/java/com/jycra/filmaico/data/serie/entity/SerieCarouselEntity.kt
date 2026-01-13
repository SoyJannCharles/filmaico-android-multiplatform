package com.jycra.filmaico.data.serie.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "serie_carousels")
data class SerieCarouselEntity(
    @PrimaryKey
    val id: String,
    val title: String,
    val order: Int,
    val queryType: String,
    val queryValueJson: String
)