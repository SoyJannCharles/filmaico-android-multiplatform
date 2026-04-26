package com.jycra.filmaico.data.user.data.source

import com.google.firebase.FirebaseNetworkException
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.jycra.filmaico.core.model.user.AuthTokenDto
import com.jycra.filmaico.core.model.user.SessionDto
import com.jycra.filmaico.core.model.user.SubscriptionDto
import com.jycra.filmaico.core.model.user.UserDto
import com.jycra.filmaico.domain.user.error.AuthError
import com.jycra.filmaico.domain.user.model.AuthStatus
import com.jycra.filmaico.domain.user.util.AuthResult
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import java.io.IOException
import java.util.Date
import javax.inject.Inject

class UserSource @Inject constructor(
    private val firestore: FirebaseFirestore
) {

    private val collection = "users"

    suspend fun createAuthSession(code: String): AuthResult<String, AuthError> {

        val expirationMillis = System.currentTimeMillis() + (15 * 60 * 1000)
        val expiresAtTimestamp = Timestamp(Date(expirationMillis))

        return try {
            firestore.collection("auth_codes")
                .document(code)
                .set(AuthTokenDto(status = "pending", expiresAt = expiresAtTimestamp))
                .await()
            AuthResult.Success(code)
        } catch (e: Exception) {
            AuthResult.Failure(
                when (e) {
                    is FirebaseNetworkException, is IOException -> AuthError.NetworkError
                    is FirebaseFirestoreException -> {
                        when (e.code) {
                            FirebaseFirestoreException.Code.PERMISSION_DENIED -> AuthError.PermissionDenied
                            FirebaseFirestoreException.Code.UNAVAILABLE -> AuthError.NetworkError
                            else -> AuthError.ServerError
                        }
                    }
                    else -> AuthError.Unknown
                }
            )
        }

    }

    suspend fun authorizeSessionWithUid(code: String, uid: String): AuthResult<Unit, AuthError> {

        try {

            val sessionRef = firestore.collection("auth_codes").document(code)

            val snapshot = sessionRef.get().await()

            if (!snapshot.exists()) {
                return AuthResult.Failure(AuthError.PermissionDenied)
            }

            sessionRef.update(
                "uid", uid,
                "status", AuthStatus.COMPLETED.value
            ).await()

            return AuthResult.Success(Unit)

        } catch (e: Exception) {
            val error = when (e) {
                is FirebaseNetworkException, is IOException -> AuthError.NetworkError
                is FirebaseFirestoreException -> {
                    when (e.code) {
                        FirebaseFirestoreException.Code.PERMISSION_DENIED -> AuthError.PermissionDenied
                        FirebaseFirestoreException.Code.UNAVAILABLE -> AuthError.NetworkError
                        else -> AuthError.ServerError
                    }
                }
                else -> AuthError.Unknown
            }
            return AuthResult.Failure(error)
        }

    }

    fun getAuthSessionReference(code: String): DocumentReference {
        return firestore.collection("auth_codes").document(code)
    }

    suspend fun deleteAuthSession(code: String): AuthResult<Unit, AuthError> {
        return try {
            firestore.collection("auth_codes")
                .document(code).delete().await()
            AuthResult.Success(Unit)
        } catch (e: Exception) {
            AuthResult.Failure(AuthError.Unknown)
        }
    }

    fun observeUser(uid: String): Flow<UserDto?> = callbackFlow {

        val docRef = firestore.collection(collection).document(uid)

        val subscription = docRef.addSnapshotListener { snapshot, error ->

            if (error != null) {
                close(error)
                return@addSnapshotListener
            }

            val user = snapshot?.toObject(UserDto::class.java)

            trySend(user)

        }

        awaitClose { subscription.remove() }

    }

    suspend fun getUser(uid: String): UserDto? {
        return try {
            firestore.collection(collection).document(uid)
                .get().await().toObject(UserDto::class.java)
        } catch (e: Exception) {
            null
        }
    }

    fun getUserReference(uid: String): DocumentReference {
        return firestore.collection(collection).document(uid)
    }

    suspend fun addUser(uid: String, email: String): AuthResult<Unit, AuthError> {

        val nowInSeconds = System.currentTimeMillis() / 1000
        val threeHoursInSeconds = 3 * 60 * 60
        val expirationTimestamp = Timestamp(nowInSeconds + threeHoursInSeconds, 0)

        val trial = SubscriptionDto(
            maxDevices = 3,
            expirationDate = expirationTimestamp,
            createdAt = null,
            updatedAt = null
        )

        return try {
            firestore.collection(collection)
                .document(uid).set(UserDto(email = email, subscription = trial)).await()
            AuthResult.Success(Unit)
        } catch (e: Exception) {
            AuthResult.Failure(
                when (e) {
                    is FirebaseNetworkException, is IOException -> AuthError.NetworkError
                    is FirebaseFirestoreException -> {
                        when (e.code) {
                            FirebaseFirestoreException.Code.PERMISSION_DENIED -> AuthError.PermissionDenied
                            FirebaseFirestoreException.Code.UNAVAILABLE -> AuthError.NetworkError
                            else -> AuthError.ServerError
                        }
                    }
                    else -> AuthError.Unknown
                }
            )
        }

    }

    suspend fun updateUser(uid: String, user: UserDto): AuthResult<Unit, AuthError> {
        return try {
            firestore.collection(collection)
                .document(uid).set(user).await()
            AuthResult.Success(Unit)
        } catch (e: Exception) {
            AuthResult.Failure(AuthError.Unknown)
        }
    }

    suspend fun updateSessionsAtomically(
        uid: String,
        sessionUpdateBlock: (UserDto) -> List<SessionDto>
    ): AuthResult<Unit, AuthError> {

        val userRef = firestore.collection(collection).document(uid)

        return try {
            firestore.runTransaction { transaction ->

                val snapshot = transaction.get(userRef)
                val userDto = snapshot.toObject(UserDto::class.java)
                    ?: throw FirebaseFirestoreException("User not found", FirebaseFirestoreException.Code.NOT_FOUND)

                val newSessionsList = sessionUpdateBlock(userDto)

                transaction.update(userRef, "activeSessions", newSessionsList)

            }.await()
            AuthResult.Success(Unit)
        } catch (e: Exception) {
            AuthResult.Failure(AuthError.Unknown)
        }

    }

    suspend fun deleteUser(uid: String): AuthResult<Unit, AuthError> {
        return try {
            firestore.collection(collection)
                .document(uid).delete().await()
            AuthResult.Success(Unit)
        } catch (e: Exception) {
            AuthResult.Failure(AuthError.Unknown)
        }
    }

}