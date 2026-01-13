package com.jycra.filmaico.domain.movie.model

data class MovieCarousel(
    val id: String,
    val title: Map<String, String>,
    val movies: List<Movie>
)