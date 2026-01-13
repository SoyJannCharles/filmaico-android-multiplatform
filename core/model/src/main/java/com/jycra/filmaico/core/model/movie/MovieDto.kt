package com.jycra.filmaico.core.model.movie

import androidx.annotation.Keep
import com.google.firebase.firestore.DocumentId
import com.jycra.filmaico.core.model.stream.StreamDto

@Keep
data class MovieDto(
    @DocumentId
    val id: String? = null,
    val name: Map<String, String>? = null,
    val synopsis: Map<String, String>? = null,
    val duration: Long? = null,
    val coverUrl: String? = null,
    val releaseYear: Int? = null,
    val tags: List<String>? = null,
    val sources: List<StreamDto>? = null
)