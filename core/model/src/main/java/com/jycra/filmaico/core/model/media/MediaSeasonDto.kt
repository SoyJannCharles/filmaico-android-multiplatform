package com.jycra.filmaico.core.model.media

import androidx.annotation.Keep
import com.google.firebase.firestore.DocumentId

@Keep
data class MediaSeasonDto(
    @DocumentId
    val id: String? = null,
    val seasonNumber: Int = 0,
    val name: Map<String, String> = emptyMap(),
    val content: List<MediaDto> = emptyList()
)