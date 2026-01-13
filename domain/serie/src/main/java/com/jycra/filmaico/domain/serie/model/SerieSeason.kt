package com.jycra.filmaico.domain.serie.model

data class SerieSeason(
    val id: String,
    val seasonNumber: Int,
    val name: Map<String, String>,
    val content: List<SerieContent>
)