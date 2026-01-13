package com.jycra.filmaico.data.serie.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "serie_tags")
data class SerieTagEntity(
    @PrimaryKey
    val tagName: String
)