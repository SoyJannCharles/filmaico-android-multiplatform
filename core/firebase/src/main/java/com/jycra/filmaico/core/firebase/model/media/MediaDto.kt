package com.jycra.filmaico.core.firebase.model.media

import androidx.annotation.Keep
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.IgnoreExtraProperties
import com.jycra.filmaico.core.firebase.model.stream.StreamDto

@Keep
@IgnoreExtraProperties
data class MediaDto(
    @DocumentId
    val id: String? = null,
    val type: String = "episode",
    val name: Map<String, String> = emptyMap(),
    val synopsis: Map<String, String> = emptyMap(),
    val thumbnailUrl: String = "",
    val duration: Long = 0L,
    val number: Int = 0,
    val airDate: Timestamp? = null,
    val sources: List<StreamDto> = emptyList()
)