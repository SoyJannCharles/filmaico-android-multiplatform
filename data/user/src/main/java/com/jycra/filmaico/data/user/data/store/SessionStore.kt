package com.jycra.filmaico.data.user.data.store

interface SessionStore {

    suspend fun getSessionId(): String?

    suspend fun saveSessionId(sessionId: String)

    suspend fun clearSessionId()

}