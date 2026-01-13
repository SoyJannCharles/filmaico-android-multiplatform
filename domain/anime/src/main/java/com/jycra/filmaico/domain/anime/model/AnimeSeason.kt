package com.jycra.filmaico.domain.anime.model

data class AnimeSeason(
    val id: String,
    val seasonNumber: Int,
    val name: Map<String, String>,
    val content: List<AnimeContent> = emptyList()
)