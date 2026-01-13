package com.jycra.filmaico.domain.anime.model

import com.jycra.filmaico.domain.stream.model.ContentStatus

data class Anime(
    val id: String = "",
    val name: Map<String, String> = emptyMap(),
    val synopsis: Map<String, String> = emptyMap(),
    val coverUrl: String = "",
    val releaseYear: Int = 0,
    val status: ContentStatus = ContentStatus.fromValue(""),
    val tags: List<String> = emptyList(),
    val seasons: List<AnimeSeason> = emptyList()
)