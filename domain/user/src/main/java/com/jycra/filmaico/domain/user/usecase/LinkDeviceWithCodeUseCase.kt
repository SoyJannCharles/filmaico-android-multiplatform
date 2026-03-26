package com.jycra.filmaico.domain.user.usecase

import com.jycra.filmaico.domain.user.error.AuthError
import com.jycra.filmaico.domain.user.repository.AuthRepository
import com.jycra.filmaico.domain.user.util.AuthResult
import javax.inject.Inject

class LinkDeviceWithCodeUseCase @Inject constructor(
    private val repository: AuthRepository
) {

    suspend operator fun invoke(code: String): AuthResult<Unit, AuthError> =
        repository.linkDeviceWithCode(code)

}