package com.jycra.filmaico.data.anime.entity

import androidx.room.Entity

@Entity(tableName = "anime_tag_cross_ref", primaryKeys = ["animeId", "tagName"])
data class AnimeTagCrossRef(
    val animeId: String,
    val tagName: String
)
