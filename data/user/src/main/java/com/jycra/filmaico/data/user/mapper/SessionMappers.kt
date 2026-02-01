package com.jycra.filmaico.data.user.mapper

import com.jycra.filmaico.core.model.user.SessionDto
import com.jycra.filmaico.domain.user.model.Session

fun SessionDto.toDomain(): Session {
    return Session(
        sessionId = this.sessionId,
        loginDate = this.loginDate.toDate(),
        deviceId = this.deviceId,
        deviceInfo = this.deviceInfo
    )
}