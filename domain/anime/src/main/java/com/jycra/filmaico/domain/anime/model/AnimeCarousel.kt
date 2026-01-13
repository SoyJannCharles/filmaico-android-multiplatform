package com.jycra.filmaico.domain.anime.model

data class AnimeCarousel(
    val id: String,
    val title: Map<String, String>,
    val animes: List<Anime>
)