package com.jycra.filmaico.domain.user.usecase

import com.jycra.filmaico.domain.user.model.AuthToken
import com.jycra.filmaico.domain.user.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveAuthSessionStatusUseCase @Inject constructor(
    private val repository: AuthRepository
) {

    operator fun invoke(code: String): Flow<AuthToken> {
        return repository.observeAuthSessionStatus(code)
    }

}