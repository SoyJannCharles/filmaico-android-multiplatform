package com.jycra.filmaico.data.user.util

import kotlinx.coroutines.flow.Flow

interface SessionObserver {

    fun observe(): Flow<SessionStatus>

    enum class SessionStatus {
        Valid,
        Invalid,
        Loading
    }

}