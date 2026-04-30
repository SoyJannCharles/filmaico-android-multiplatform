package com.jycra.filmaico.core.app

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jycra.filmaico.domain.network.ConnectivityObserver
import com.jycra.filmaico.domain.user.util.SessionStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class AppViewModel @Inject constructor(
    private val sessionManager: SessionManager
) : ViewModel() {

    val appHealth: StateFlow<AppHealth> = sessionManager.appHealth
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), AppHealth.Checking)

    val networkStatus: StateFlow<ConnectivityObserver.Status> = sessionManager.networkStatus
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ConnectivityObserver.Status.Available)

    val sessionStatus: StateFlow<SessionStatus> = sessionManager.sessionStatus
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), SessionStatus.Checking)

    val isSubscriptionActive: StateFlow<Boolean> = sessionStatus
        .flatMapLatest { status ->
            if (status is SessionStatus.Authenticated) {
                tickerFlow(30_000).map {
                    status.user.subscription?.isActive() == true
                }
            } else {
                flowOf(false)
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), true)

    private fun tickerFlow(period: Long) = flow {
        while (true) { emit(Unit); delay(period) }
    }

}