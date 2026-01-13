package com.jycra.filmaico.core.model.serie

import androidx.annotation.Keep
import com.google.firebase.firestore.DocumentId

@Keep
data class SerieSeasonDto(
    @DocumentId
    val id: String? = null,
    val seasonNumber: Int? = null,
    val name: Map<String, String>? = null,
    val content: List<SerieContentDto>? = null
)