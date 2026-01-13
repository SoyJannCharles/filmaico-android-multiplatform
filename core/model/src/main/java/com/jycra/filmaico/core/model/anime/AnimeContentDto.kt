package com.jycra.filmaico.core.model.anime

import androidx.annotation.Keep
import com.jycra.filmaico.core.model.stream.StreamDto

@Keep
data class AnimeContentDto(
    val id: String? = null,
    val type: String? = null, // "episode", "movie", "ova"
    val order: Int? = null,
    val name: Map<String, String>? = null,
    val duration: Long? = null,
    val thumbnailUrl: String? = null,
    val episodeNumber: Int? = null,
    val sources: List<StreamDto>? = null
)