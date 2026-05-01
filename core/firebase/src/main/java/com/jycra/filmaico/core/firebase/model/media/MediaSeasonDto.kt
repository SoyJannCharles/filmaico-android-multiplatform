package com.jycra.filmaico.core.firebase.model.media

import androidx.annotation.Keep
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.IgnoreExtraProperties

@Keep
@IgnoreExtraProperties
data class MediaSeasonDto(
    @DocumentId
    val id: String? = null,
    val number: Int = 0,
    val name: Map<String, String> = emptyMap(),
    val content: List<MediaDto> = emptyList()
)