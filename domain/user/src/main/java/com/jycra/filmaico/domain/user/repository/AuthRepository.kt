package com.jycra.filmaico.domain.user.repository

import com.jycra.filmaico.domain.user.error.AuthError
import com.jycra.filmaico.domain.user.model.AuthToken
import com.jycra.filmaico.domain.user.util.AuthResult
import com.jycra.filmaico.domain.user.util.SessionStatus
import kotlinx.coroutines.flow.Flow

interface AuthRepository {

    suspend fun registerAccount(email: String, password: String): AuthResult<Unit, AuthError>
    suspend fun authenticateWithEmail(email: String, password: String): AuthResult<Unit, AuthError>
    suspend fun authenticateWithToken(code: String, token: String): AuthResult<Unit, AuthError>

    fun observeAuthSessionStatus(code: String): Flow<AuthToken>
    suspend fun createAuthSession(): AuthResult<String, AuthError>
    suspend fun linkDeviceWithCode(code: String): AuthResult<Unit, AuthError>

    suspend fun signOut()
    suspend fun signOutLocal()

    fun observeSessionStatus(): Flow<SessionStatus>

    suspend fun hasActiveSubscription(): Boolean
    fun observeSubscriptionStatus(): Flow<Boolean>

    suspend fun registerDeviceSession(): AuthResult<Unit, AuthError>

    suspend fun deleteCurrentUser(): AuthResult<Unit, AuthError>
    suspend fun reauthenticateAndDelete(password: String): AuthResult<Unit, AuthError>

}