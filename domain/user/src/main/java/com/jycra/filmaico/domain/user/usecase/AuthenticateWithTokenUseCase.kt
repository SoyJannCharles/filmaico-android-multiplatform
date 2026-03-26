package com.jycra.filmaico.domain.user.usecase

import com.jycra.filmaico.domain.user.error.AuthError
import com.jycra.filmaico.domain.user.repository.AuthRepository
import com.jycra.filmaico.domain.user.util.AuthResult
import javax.inject.Inject

class AuthenticateWithTokenUseCase @Inject constructor(
    private val repository: AuthRepository
) {

    suspend operator fun invoke(code: String, token: String): AuthResult<Unit, AuthError> {
        return repository.authenticateWithToken(code, token)
    }

}