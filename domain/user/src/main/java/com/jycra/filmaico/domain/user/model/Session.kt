package com.jycra.filmaico.domain.user.model

import java.util.Date

data class Session(
    val sessionId: String,
    val loginDate: Date,
    val deviceId: String,
    val deviceInfo: String
)