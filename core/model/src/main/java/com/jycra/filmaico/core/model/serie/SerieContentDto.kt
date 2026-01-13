package com.jycra.filmaico.core.model.serie

import androidx.annotation.Keep
import com.google.firebase.firestore.DocumentId
import com.jycra.filmaico.core.model.stream.StreamDto

@Keep
data class SerieContentDto(
    @DocumentId
    val id: String? = null,
    val type: String? = null, // "episode", "movie", "ova"
    val order: Int? = null,
    val name: Map<String, String>? = null,
    val duration: Long? = null,
    val thumbnailUrl: String? = null,
    val contentNumber: Int? = null,
    val sources: List<StreamDto>? = null
)