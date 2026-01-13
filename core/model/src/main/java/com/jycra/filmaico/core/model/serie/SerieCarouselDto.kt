package com.jycra.filmaico.core.model.serie

import androidx.annotation.Keep
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.IgnoreExtraProperties

@Keep
@IgnoreExtraProperties
data class SerieCarouselDto(
    @DocumentId
    val id: String? = null,
    val title: Map<String, String>? = null,
    val order: Int? = null,
    val queryType: String? = null,
    val queryValue: Any? = null
)