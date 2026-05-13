package com.jycra.filmaico.core.firebase.model.stream

import androidx.annotation.Keep
import com.google.firebase.Timestamp
import com.google.firebase.firestore.IgnoreExtraProperties
import com.google.firebase.firestore.ServerTimestamp

@Keep
@IgnoreExtraProperties
data class EdgeDto(
    val channel: String? = null,
    val stableKey: String? = null,
    val url: String? = null,
    val edgeHost: String? = null,
    val cluster: String? = null,
    val expiresAt: Timestamp? = null,
    @ServerTimestamp
    val lastUsed: Timestamp? = null
)