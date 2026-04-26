package com.jycra.filmaico.core.app

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jycra.filmaico.core.config.ConfigSource
import com.jycra.filmaico.domain.network.ConnectivityObserver
import com.jycra.filmaico.domain.user.usecase.ObserveSessionStatusUseCase
import com.jycra.filmaico.domain.user.util.SessionStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AppViewModel @Inject constructor(
    private val configSource: ConfigSource,
    private val appVersionCode: Long,
    private val appVersionName: String,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _appHealth = MutableStateFlow<AppHealth>(AppHealth.Checking)
    val appHealth = _appHealth.asStateFlow()

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

    init {
        checkAppHealth()
    }

    private fun checkAppHealth() {
        viewModelScope.launch {
            try {
                val serverVersionCode = configSource.getAppVersionCode()
                if (appVersionCode < serverVersionCode) {
                    _appHealth.value = AppHealth.UpdateRequired(
                        current = appVersionName,
                        server = configSource.getAppVersionName()
                    )
                } else {
                    _appHealth.value = AppHealth.Ready
                }
            } catch (e: Exception) {
                _appHealth.value = AppHealth.Error("Error al validar versión")
            }
        }
    }

    private fun tickerFlow(period: Long) = flow {
        while (true) { emit(Unit); delay(period) }
    }

    fun signOut() {
        /*viewModelScope.launch {
            signOutLocalUseCase()
        }*/
    }

}