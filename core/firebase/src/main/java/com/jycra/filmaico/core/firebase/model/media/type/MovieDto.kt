package com.jycra.filmaico.core.firebase.model.media.type

import androidx.annotation.Keep
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.IgnoreExtraProperties
import com.jycra.filmaico.core.firebase.model.stream.StreamDto

@Keep
@IgnoreExtraProperties
data class MovieDto(
    @DocumentId
    val id: String? = null,
    val name: Map<String, String> = emptyMap(),
    val synopsis: Map<String, String> = emptyMap(),
    val posterUrl: Map<String, String> = emptyMap(),
    val airDate: Timestamp? = null,
    val duration: Long = 0L,
    val tags: List<String> = emptyList(),
    val sources: List<StreamDto> = emptyList()
)