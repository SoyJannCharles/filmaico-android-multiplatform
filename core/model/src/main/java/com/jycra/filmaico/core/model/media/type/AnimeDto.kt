package com.jycra.filmaico.core.model.media.type

import androidx.annotation.Keep
import com.google.firebase.firestore.DocumentId
import com.jycra.filmaico.core.model.media.MediaSeasonDto

@Keep
data class AnimeDto(
    @DocumentId
    val id: String? = null,
    val name: Map<String, String> = emptyMap(),
    val synopsis: Map<String, String> = emptyMap(),
    val coverUrl: String = "",
    val releaseYear: Int? = null,
    val status: String = "Unknown",
    val tags: List<String> = emptyList(),
    val seasons: List<MediaSeasonDto> = emptyList()
)