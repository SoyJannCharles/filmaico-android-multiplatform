package com.jycra.filmaico.domain.serie.model

data class SerieCarousel(
    val id: String,
    val title: Map<String, String>,
    val series: List<Serie>
)