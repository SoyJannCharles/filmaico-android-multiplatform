package com.jycra.filmaico.core.model.media.type

import androidx.annotation.Keep
import com.google.firebase.firestore.DocumentId
import com.jycra.filmaico.core.model.stream.StreamDto

@Keep
data class MovieDto(
    @DocumentId
    val id: String? = null,
    val name: Map<String, String> = emptyMap(),
    val synopsis: Map<String, String> = emptyMap(),
    val coverUrl: String = "",
    val duration: Long = 0L,
    val releaseYear: Int? = null,
    val tags: List<String> = emptyList(),
    val sources: List<StreamDto> = emptyList()
)