package com.jycra.filmaico.core.firebase.model.stream

import androidx.annotation.Keep
import com.google.firebase.Timestamp
import com.google.firebase.firestore.IgnoreExtraProperties
import com.google.firebase.firestore.ServerTimestamp

@Keep
@IgnoreExtraProperties
data class SeedDto(
    val domain: String? = null,
    val provider: String? = null,
    @ServerTimestamp
    val lastDiscovery: Timestamp? = null
)