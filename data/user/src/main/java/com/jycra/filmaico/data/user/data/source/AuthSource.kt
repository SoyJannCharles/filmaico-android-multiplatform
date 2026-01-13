package com.jycra.filmaico.data.user.data.source

import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthRecentLoginRequiredException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.auth.FirebaseUser
import com.jycra.filmaico.domain.user.error.AuthError
import com.jycra.filmaico.domain.user.util.AuthResult
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.io.IOException
import javax.inject.Inject

class AuthSource @Inject constructor(
    private val auth: FirebaseAuth,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) {

    fun getCurrentUser(): FirebaseUser? = auth.currentUser

    suspend fun signUpWithEmail(
        email: String,
        password: String
    ): AuthResult<FirebaseUser, AuthError> = withContext(ioDispatcher) {
        try {
            val user = auth.createUserWithEmailAndPassword(email, password).await().user
            user?.let { AuthResult.Success(it) }
                ?: AuthResult.Failure(AuthError.NullUserAfterAuthSuccess)
        } catch (e: Exception) {
            AuthResult.Failure(mapFirebaseAuthException(e))
        }
    }

    suspend fun signInWithEmail(
        email: String,
        password: String
    ): AuthResult<FirebaseUser, AuthError> = withContext(ioDispatcher) {
        try {
            val user = auth.signInWithEmailAndPassword(email, password).await().user
            user?.let { AuthResult.Success(it) }
                ?: AuthResult.Failure(AuthError.NullUserAfterAuthSuccess)
        } catch (e: Exception) {
            AuthResult.Failure(mapFirebaseAuthException(e))
        }
    }

    fun signOut() = auth.signOut()

    suspend fun deleteUser(user: FirebaseUser): AuthResult<Unit, AuthError> = withContext(ioDispatcher) {
        try {
            user.delete().await()
            AuthResult.Success(Unit)
        } catch (e: Exception) {
            AuthResult.Failure(mapFirebaseAuthException(e))
        }
    }

    suspend fun reauthenticateUser(password: String): AuthResult<Unit, AuthError> =
        withContext(ioDispatcher) {

            val user =
                auth.currentUser ?: return@withContext AuthResult.Failure(AuthError.UserNotFound)
            val email = user.email ?: return@withContext AuthResult.Failure(AuthError.UserNotFound)

            try {
                val credential = EmailAuthProvider.getCredential(email, password)
                user.reauthenticate(credential).await()
                AuthResult.Success(Unit)
            } catch (e: Exception) {
                AuthResult.Failure(mapFirebaseAuthException(e))
            }

        }

    private fun mapFirebaseAuthException(e: Exception): AuthError {
        return when (e) {
            is FirebaseAuthUserCollisionException -> AuthError.EmailAlreadyInUse
            is FirebaseAuthWeakPasswordException -> AuthError.WeakPassword
            is FirebaseAuthInvalidCredentialsException -> AuthError.InvalidCredentials
            is FirebaseAuthInvalidUserException -> AuthError.UserNotFound
            is FirebaseAuthRecentLoginRequiredException -> AuthError.RequiresRecentLogin
            is FirebaseNetworkException -> AuthError.NetworkError
            is IOException -> AuthError.NetworkError
            is FirebaseAuthException -> {
                when (e.errorCode) {
                    "ERROR_TOO_MANY_REQUESTS" -> AuthError.TooManyRequests
                    "ERROR_USER_DISABLED" -> AuthError.AccountDisabled
                    else -> AuthError.ServerError
                }
            }
            else -> AuthError.Unknown
        }
    }

}