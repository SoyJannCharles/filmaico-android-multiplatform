package com.jycra.filmaico.core.model.anime

import androidx.annotation.Keep
import com.google.firebase.firestore.DocumentId

@Keep
data class AnimeDto(
    @DocumentId
    val id: String? = null,
    val name: Map<String, String>? = null,
    val synopsis: Map<String, String>? = null,
    val coverUrl: String? = null,
    val releaseYear: Int? = null,
    val status: String? = null,
    val tags: List<String>? = null,
    val seasons: List<AnimeSeasonDto>? = null
)
