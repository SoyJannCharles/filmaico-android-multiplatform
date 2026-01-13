package com.jycra.filmaico.data.user.data.source

import com.google.firebase.FirebaseNetworkException
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.Transaction
import com.jycra.filmaico.core.model.user.SessionDto
import com.jycra.filmaico.core.model.user.UserDto
import com.jycra.filmaico.domain.user.error.AuthError
import com.jycra.filmaico.domain.user.util.AuthResult
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import java.io.IOException
import javax.inject.Inject

class UserSource @Inject constructor(
    private val firestore: FirebaseFirestore
) {

    private val collection = "users"

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
        return try {
            firestore.collection(collection)
                .document(uid).set(UserDto(email = email)).await()
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