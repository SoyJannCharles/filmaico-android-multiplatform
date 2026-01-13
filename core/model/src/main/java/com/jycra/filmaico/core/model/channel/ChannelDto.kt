package com.jycra.filmaico.core.model.channel

import androidx.annotation.Keep
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.IgnoreExtraProperties
import com.jycra.filmaico.core.model.stream.StreamDto

@Keep
@IgnoreExtraProperties
data class ChannelDto(
    @DocumentId
    val id: String? = null,
    val name: Map<String, String>? = null,
    val iconUrl: String? = null,
    val tags: List<String>? = null,
    val sources: List<StreamDto>? = null
)