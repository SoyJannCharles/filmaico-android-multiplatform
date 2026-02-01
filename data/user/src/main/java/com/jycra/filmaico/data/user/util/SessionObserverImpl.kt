package com.jycra.filmaico.data.user.util

import com.jycra.filmaico.domain.user.usecase.ObserveSessionStatusUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SessionObserverImpl @Inject constructor(
    private val observeSessionStatusUseCase: ObserveSessionStatusUseCase
) : SessionObserver {

    override fun observe(): Flow<SessionObserver.SessionStatus> {
        return observeSessionStatusUseCase()
            .map { isValid ->
                if (isValid) SessionObserver.SessionStatus.Valid
                else SessionObserver.SessionStatus.Invalid
            }
            .catch { emit(SessionObserver.SessionStatus.Invalid) }
    }

}