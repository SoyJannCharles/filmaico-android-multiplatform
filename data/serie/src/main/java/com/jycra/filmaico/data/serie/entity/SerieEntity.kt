package com.jycra.filmaico.data.serie.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "series")
data class SerieEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val synopsis: String,
    val coverUrl: String,
    val releaseYear: Int,
    val status: String
)