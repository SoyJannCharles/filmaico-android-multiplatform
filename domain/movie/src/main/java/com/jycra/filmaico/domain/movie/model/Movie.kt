package com.jycra.filmaico.domain.movie.model

import com.jycra.filmaico.domain.stream.model.Stream

data class Movie(
    val id: String = "",
    val name: Map<String, String> = emptyMap(),
    val synopsis: Map<String, String> = emptyMap(),
    val coverUrl: String = "",
    val duration: Long,
    val releaseYear: Int = 0,
    val tags: List<String> = emptyList(),
    val sources: List<Stream> = emptyList()
)