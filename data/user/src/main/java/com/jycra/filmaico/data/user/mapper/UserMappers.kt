package com.jycra.filmaico.data.user.mapper

import com.jycra.filmaico.core.model.user.UserDto
import com.jycra.filmaico.domain.user.model.User

fun UserDto.toDomain(): User {
    return User(
        uid = this.uid,
        email = this.email,
        subscription = this.subscription?.toDomain(),
        activeSessions = this.activeSessions.map { it.toDomain() }
    )
}