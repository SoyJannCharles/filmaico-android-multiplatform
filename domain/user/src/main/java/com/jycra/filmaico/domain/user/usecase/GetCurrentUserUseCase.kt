package com.jycra.filmaico.domain.user.usecase

import com.jycra.filmaico.domain.user.model.User
import com.jycra.filmaico.domain.user.repository.AuthRepository
import javax.inject.Inject

class GetCurrentUserUseCase @Inject constructor(
    private val repository: AuthRepository
) {

    suspend operator fun invoke() : User? =
        repository.getCurrentUser()

}