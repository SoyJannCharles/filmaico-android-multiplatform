package com.jycra.filmaico.domain.user.repository

import com.jycra.filmaico.domain.user.error.AuthError
import com.jycra.filmaico.domain.user.model.User
import com.jycra.filmaico.domain.user.util.AuthResult
import kotlinx.coroutines.flow.Flow

interface AuthRepository {

    suspend fun signUp(email: String, password: String): AuthResult<Unit, AuthError>
    suspend fun signIn(email: String, password: String): AuthResult<Unit, AuthError>
    suspend fun signOut()

    suspend fun getCurrentUser(): User?

    suspend fun hasActiveSubscription(): Boolean
    fun listenToSubscriptionStatus(): Flow<Boolean>

    suspend fun saveCurrentDeviceSession(): AuthResult<Unit, AuthError>

    suspend fun deleteCurrentUser(): AuthResult<Unit, AuthError>
    suspend fun reauthenticateAndDelete(password: String): AuthResult<Unit, AuthError>

}