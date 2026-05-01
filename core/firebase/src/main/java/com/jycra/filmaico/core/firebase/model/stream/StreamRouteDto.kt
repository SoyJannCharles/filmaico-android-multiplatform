package com.jycra.filmaico.core.firebase.model.stream

import androidx.annotation.Keep
import com.google.firebase.Timestamp
import com.google.firebase.firestore.IgnoreExtraProperties
import com.google.firebase.firestore.ServerTimestamp

@Keep
@IgnoreExtraProperties
data class StreamRouteDto(
    val domain: String? = null,
    val provider: String? = null,
    val avgResponseMs: Long? = null,
    val dead: Boolean = false,
    @ServerTimestamp
    val lastUsed: Timestamp? = null
)