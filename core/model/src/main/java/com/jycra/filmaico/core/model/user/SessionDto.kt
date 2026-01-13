package com.jycra.filmaico.core.model.user

import androidx.annotation.Keep
import com.google.firebase.Timestamp

@Keep
data class SessionDto(
    val sessionId: String = "",
    val loginDate: Timestamp = Timestamp.now(),
    val deviceId: String = "",
    val deviceInfo: String = ""
)