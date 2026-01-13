package com.jycra.filmaico.domain.serie.model

import com.jycra.filmaico.domain.stream.model.ContentStatus

data class Serie(
    val id: String,
    val name: Map<String, String>,
    val synopsis: Map<String, String>,
    val coverUrl: String,
    val releaseYear: Int,
    val status: ContentStatus,
    val tags: List<String>,
    val seasons: List<SerieSeason>
)