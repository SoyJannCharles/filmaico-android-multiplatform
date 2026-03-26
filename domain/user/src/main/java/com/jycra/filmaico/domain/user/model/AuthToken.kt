package com.jycra.filmaico.domain.user.model

import java.util.Date

data class AuthToken(
    val uid: String? = null,
    val status: AuthStatus = AuthStatus.PENDING,
    val token: String? = null,
    val createdAt: Date? = null,
    val updatedAt: Date? = null,
    val expiresAt: Date? = null
)