package com.jycra.filmaico.core.model.media

import androidx.annotation.Keep
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.IgnoreExtraProperties

@Keep
@IgnoreExtraProperties
data class MediaCarouselDto(
    @DocumentId
    val id: String? = null,
    val title: Map<String, String> = emptyMap(),
    val order: Int = 0,
    val queryType: String = "tag",
    val queryValue: Any? = null
)