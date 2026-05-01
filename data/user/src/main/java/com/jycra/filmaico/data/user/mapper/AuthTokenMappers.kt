package com.jycra.filmaico.data.user.mapper

import com.jycra.filmaico.core.firebase.model.user.AuthTokenDto
import com.jycra.filmaico.domain.user.model.AuthStatus
import com.jycra.filmaico.domain.user.model.AuthToken

fun AuthTokenDto.toDomain(): AuthToken {
    return AuthToken(
        uid = this.uid,
        status = AuthStatus.fromString(this.status),
        token = this.token,
        createdAt = this.createdAt?.toDate(),
        updatedAt = this.updatedAt?.toDate(),
        expiresAt = this.expiresAt?.toDate()
    )
}