package com.jycra.filmaico.core.firebase.model.media.type

import androidx.annotation.Keep
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.IgnoreExtraProperties
import com.jycra.filmaico.core.firebase.model.stream.StreamDto

@Keep
@IgnoreExtraProperties
data class ChannelDto(
    @DocumentId
    val id: String? = null,
    val name: Map<String, String> = emptyMap(),
    val iconUrl: String = "",
    val epgId: String = "",
    val tags: List<String> = emptyList(),
    val sources: List<StreamDto> = emptyList()
)