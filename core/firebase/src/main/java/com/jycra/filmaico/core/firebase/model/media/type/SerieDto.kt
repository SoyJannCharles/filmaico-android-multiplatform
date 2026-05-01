package com.jycra.filmaico.core.firebase.model.media.type

import androidx.annotation.Keep
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.IgnoreExtraProperties
import com.jycra.filmaico.core.firebase.model.media.MediaSeasonDto

@Keep
@IgnoreExtraProperties
data class SerieDto(
    @DocumentId
    val id: String? = null,
    val name: Map<String, String> = emptyMap(),
    val synopsis: Map<String, String> = emptyMap(),
    val posterUrl: Map<String, String> = emptyMap(),
    val firstAirDate: Timestamp? = null,
    val lastAirDate: Timestamp? = null,
    val status: String = "Unknown",
    val tags: List<String> = emptyList(),
    val seasons: List<MediaSeasonDto> = emptyList(),
)