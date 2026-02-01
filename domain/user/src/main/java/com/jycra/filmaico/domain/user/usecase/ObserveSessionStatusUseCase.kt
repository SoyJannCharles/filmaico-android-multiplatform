package com.jycra.filmaico.domain.user.usecase

import com.jycra.filmaico.domain.user.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveSessionStatusUseCase @Inject constructor(
    private val repository: AuthRepository
) {

    operator fun invoke(): Flow<Boolean> =
        repository.observeSessionStatus()

}