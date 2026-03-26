package com.jycra.filmaico.data.user.mapper

import com.jycra.filmaico.core.model.user.SessionDto
import com.jycra.filmaico.domain.user.model.Session
import java.util.Date

fun SessionDto.toDomain(): Session {
    return Session(
        sessionId = this.sessionId,
        loginDate = this.loginDate?.toDate() ?: Date(),
        deviceId = this.deviceId,
        deviceInfo = this.deviceInfo
    )
}