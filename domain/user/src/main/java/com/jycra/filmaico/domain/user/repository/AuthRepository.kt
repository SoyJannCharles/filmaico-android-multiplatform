package com.jycra.filmaico.domain.user.repository

import com.jycra.filmaico.domain.user.error.AuthError
import com.jycra.filmaico.domain.user.model.User
import com.jycra.filmaico.domain.user.util.AuthResult
import kotlinx.coroutines.flow.Flow

interface AuthRepository {

    suspend fun registerAccount(email: String, password: String): AuthResult<Unit, AuthError>
    suspend fun authenticate(email: String, password: String): AuthResult<Unit, AuthError>
    suspend fun signOut()
    suspend fun signOutLocal()

    suspend fun getCurrentUser(): User?

    suspend fun hasActiveSubscription(): Boolean
    fun observeSessionStatus(): Flow<Boolean>
    fun observeSubscriptionStatus(): Flow<Boolean>

    suspend fun registerDeviceSession(): AuthResult<Unit, AuthError>

    suspend fun deleteCurrentUser(): AuthResult<Unit, AuthError>
    suspend fun reauthenticateAndDelete(password: String): AuthResult<Unit, AuthError>

}