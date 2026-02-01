package com.jycra.filmaico.data.user.repository

import android.os.Build
import com.google.firebase.Timestamp
import com.google.firebase.firestore.snapshots
import com.google.firebase.firestore.toObject
import com.jycra.filmaico.core.model.user.SessionDto
import com.jycra.filmaico.core.model.user.UserDto
import com.jycra.filmaico.data.user.data.DeviceIdProvider
import com.jycra.filmaico.data.user.data.source.AuthSource
import com.jycra.filmaico.data.user.data.source.UserSource
import com.jycra.filmaico.data.user.data.store.SessionStore
import com.jycra.filmaico.data.user.mapper.toDomain
import com.jycra.filmaico.domain.user.error.AuthError
import com.jycra.filmaico.domain.user.model.User
import com.jycra.filmaico.domain.user.repository.AuthRepository
import com.jycra.filmaico.domain.user.util.AuthResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import java.util.UUID
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authSource: AuthSource,
    private val userSource: UserSource,
    private val sessionStore: SessionStore,
    private val deviceIdProvider: DeviceIdProvider
) : AuthRepository {

    override suspend fun registerAccount(email: String, password: String): AuthResult<Unit, AuthError> {
        return when (val authResult = authSource.signUpWithEmail(email, password)) {
            is AuthResult.Success -> {
                when (val addUserResult = userSource.addUser(authResult.data.uid, email)) {
                    is AuthResult.Success -> {
                        AuthResult.Success(Unit)
                    }
                    is AuthResult.Failure -> {
                        addUserResult
                    }
                }
            }
            is AuthResult.Failure -> {
                authResult
            }
        }
    }

    override suspend fun authenticate(email: String, password: String): AuthResult<Unit, AuthError> {
        return when (val authResult = authSource.signInWithEmail(email, password)) {
            is AuthResult.Success -> {
                AuthResult.Success(Unit)
            }
            is AuthResult.Failure -> {
                authResult
            }
        }
    }

    override suspend fun signOut() {

        val uid = authSource.getCurrentUser()?.uid
        val sessionId = sessionStore.getSessionId()

        if (uid != null && sessionId != null) {
            val user = userSource.getUser(uid)
            if (user != null) {
                userSource.updateUser(
                    uid = uid,
                    user = user.copy(
                        activeSessions = user.activeSessions.filterNot {
                            it.sessionId == sessionId
                        }
                    )
                )
            }
        }

        authSource.signOut()
        sessionStore.clearSessionId()

    }

    override suspend fun signOutLocal() {
        authSource.signOut()
        sessionStore.clearSessionId()
    }

    override suspend fun getCurrentUser(): User? {
        val account = authSource.getCurrentUser() ?: return null
        return userSource.getUser(account.uid)?.toDomain()
    }

    override suspend fun hasActiveSubscription(): Boolean {
        return userSource.getUser(
            uid = authSource.getCurrentUser()?.uid ?: return false
        )?.subscription?.isActive() == true
    }

    override fun observeSessionStatus(): Flow<Boolean> {

        val uid = authSource.getCurrentUser()?.uid ?: return flowOf(false)
        val currentDeviceId = deviceIdProvider.getDeviceId()

        val userReference = userSource.getUserReference(uid)

        return userReference.snapshots().map { documentSnapshot ->

            val userDto = documentSnapshot.toObject<UserDto>()

            val isSubscriptionActive = userDto?.subscription?.isActive() == true
            val isDeviceAuthorized = userDto?.activeSessions?.any { session ->
                session.deviceId == currentDeviceId
            } == true

            isSubscriptionActive && isDeviceAuthorized

        }.distinctUntilChanged()

    }

    override fun observeSubscriptionStatus(): Flow<Boolean> {

        val uid = authSource.getCurrentUser()?.uid ?: return flowOf(false)
        val userReference = userSource.getUserReference(uid)

        return userReference.snapshots().map { documentSnapshot ->

            val userDto = documentSnapshot.toObject<UserDto>()

            val isActive = userDto?.subscription?.isActive() == true

            isActive

        }.distinctUntilChanged()

    }

    override suspend fun registerDeviceSession(): AuthResult<Unit, AuthError> {

        val uid = authSource.getCurrentUser()?.uid
            ?: return AuthResult.Failure(AuthError.UserNotFound)
        val currentDeviceId = deviceIdProvider.getDeviceId()

        var sessionIdToSave: String? = null

        val result = userSource.updateSessionsAtomically(uid) { userDto ->

            val currentSessions = userDto.activeSessions.toMutableList()
            val maxDevices = userDto.subscription.maxDevices

            val existingSession = currentSessions.find { it.deviceId == currentDeviceId }

            if (existingSession != null) {

                sessionIdToSave = existingSession.sessionId

                currentSessions.remove(existingSession)
                currentSessions.add(existingSession.copy(loginDate = Timestamp.now()))

            } else {

                if (maxDevices > 0 && currentSessions.size >= maxDevices) {
                    currentSessions.sortBy { it.loginDate }
                    while (currentSessions.size >= maxDevices) currentSessions.removeAt(0)
                }

                sessionIdToSave = UUID.randomUUID().toString()
                currentSessions.add(
                    SessionDto(
                        sessionId = sessionIdToSave,
                        loginDate = Timestamp.now(),
                        deviceInfo = "${Build.MANUFACTURER} ${Build.MODEL}",
                        deviceId = currentDeviceId
                    )
                )

            }

            currentSessions

        }

        return when (result) {
            is AuthResult.Success -> {
                sessionIdToSave?.let { id ->
                    sessionStore.saveSessionId(sessionId = id)
                }
                AuthResult.Success(Unit)
            }
            is AuthResult.Failure -> result
        }

    }

    override suspend fun deleteCurrentUser(): AuthResult<Unit, AuthError> {

        val user = authSource.getCurrentUser()
            ?: return AuthResult.Failure(AuthError.UserNotFound)

        val authDeleteResult = authSource.deleteUser(user)
        if (authDeleteResult is AuthResult.Failure) {
            return authDeleteResult
        }

        val firestoreDeleteResult = userSource.deleteUser(user.uid)
        if (firestoreDeleteResult is AuthResult.Failure) {
            return firestoreDeleteResult
        }

        sessionStore.clearSessionId()

        return AuthResult.Success(Unit)

    }

    override suspend fun reauthenticateAndDelete(password: String): AuthResult<Unit, AuthError> {

        val reauthResult = authSource.reauthenticateUser(password)

        if (reauthResult is AuthResult.Failure) {
            return reauthResult
        }

        return deleteCurrentUser()

    }

}