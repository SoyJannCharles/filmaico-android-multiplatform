package com.jycra.filmaico.core.model.anime

import androidx.annotation.Keep

@Keep
data class AnimeSeasonDto(
    val id: String? = null,
    val seasonNumber: Int? = null,
    val name: Map<String, String>? = null,
    val content: List<AnimeContentDto>? = null
)