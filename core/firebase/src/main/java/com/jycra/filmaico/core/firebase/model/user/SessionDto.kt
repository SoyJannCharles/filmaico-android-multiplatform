package com.jycra.filmaico.core.firebase.model.user

import androidx.annotation.Keep
import com.google.firebase.Timestamp
import com.google.firebase.firestore.IgnoreExtraProperties
import com.google.firebase.firestore.ServerTimestamp

@Keep
@IgnoreExtraProperties
data class SessionDto(
    val sessionId: String = "",
    @ServerTimestamp
    val loginDate: Timestamp? = null,
    val deviceId: String = "",
    val deviceInfo: String = ""
)