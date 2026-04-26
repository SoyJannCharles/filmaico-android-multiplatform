package com.jycra.filmaico.domain.user.util

import com.jycra.filmaico.domain.user.model.User

sealed class SessionStatus {
    object Checking : SessionStatus()
    object Unauthenticated : SessionStatus()
    object MissedDocument : SessionStatus()
    data class Authenticated(val user: User) : SessionStatus()
}