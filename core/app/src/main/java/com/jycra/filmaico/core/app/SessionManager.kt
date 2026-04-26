package com.jycra.filmaico.core.app

import com.jycra.filmaico.domain.network.ConnectivityObserver
import com.jycra.filmaico.domain.user.usecase.ObserveSessionStatusUseCase
import com.jycra.filmaico.domain.user.util.SessionStatus
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SessionManager @Inject constructor(
    private val observeSessionStatusUseCase: ObserveSessionStatusUseCase,
    private val connectivityObserver: ConnectivityObserver
) {

    val sessionStatus: Flow<SessionStatus> = observeSessionStatusUseCase()

    val networkStatus: Flow<ConnectivityObserver.Status> = connectivityObserver.observe()

}