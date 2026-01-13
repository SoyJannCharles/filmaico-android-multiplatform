package com.jycra.filmaico.domain.user.usecase

import com.jycra.filmaico.domain.user.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ListenToSubscriptionStatusUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {

    operator fun invoke(): Flow<Boolean> =
        authRepository.listenToSubscriptionStatus()

}