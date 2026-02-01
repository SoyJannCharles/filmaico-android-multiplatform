package com.jycra.filmaico.domain.user.usecase

import com.jycra.filmaico.domain.user.repository.AuthRepository
import javax.inject.Inject

class ReauthenticateAndDeleteUserUseCase @Inject constructor(
    private val repository: AuthRepository
) {

    suspend operator fun invoke(password: String) =
        repository.reauthenticateAndDelete(password)

}