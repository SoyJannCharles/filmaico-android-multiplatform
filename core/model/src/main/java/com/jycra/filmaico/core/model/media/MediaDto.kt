package com.jycra.filmaico.core.model.media

import androidx.annotation.Keep
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.IgnoreExtraProperties
import com.jycra.filmaico.core.model.stream.StreamDto

@Keep
data class MediaDto(
    @DocumentId
    val id: String? = null,
    val name: Map<String, String> = emptyMap(),
    val thumbnailUrl: String = "",
    val duration: Long = 0L,
    val contentNumber: Int = 0,
    val order: Int = 0,
    val type: String = "episode",
    val sources: List<StreamDto> = emptyList()
)