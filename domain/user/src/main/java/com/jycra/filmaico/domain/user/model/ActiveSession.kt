package com.jycra.filmaico.domain.user.model

import java.util.Date

data class ActiveSession(
    val sessionId: String,
    val loginDate: Date,
    val deviceInfo: String
)
