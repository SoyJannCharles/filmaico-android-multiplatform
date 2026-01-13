package com.jycra.filmaico.data.anime.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "animes")
data class AnimeEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val synopsis: String,
    val coverUrl: String,
    val releaseYear: Int,
    val status: String
)