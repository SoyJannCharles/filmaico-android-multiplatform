package com.jycra.filmaico.core.app

import android.util.Log
import com.jycra.filmaico.core.config.ConfigSource
import com.jycra.filmaico.domain.network.ConnectivityObserver
import com.jycra.filmaico.domain.user.usecase.ObserveSessionStatusUseCase
import com.jycra.filmaico.domain.user.util.SessionStatus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SessionManager @Inject constructor(
    private val observeSessionStatusUseCase: ObserveSessionStatusUseCase,
    private val connectivityObserver: ConnectivityObserver,
    private val configSource: ConfigSource,
    private val appVersionCode: Long,
    private val appVersionName: String,
) {

    private val _appHealth = MutableStateFlow<AppHealth>(AppHealth.Checking)
    val appHealth: StateFlow<AppHealth> = _appHealth.asStateFlow()

    val sessionStatus: Flow<SessionStatus> = observeSessionStatusUseCase()

    val networkStatus: Flow<ConnectivityObserver.Status> = connectivityObserver.observe()

    init {
        checkHealth()
    }

    fun checkHealth() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val serverCode = configSource.getAppVersionCode()
                if (appVersionCode < serverCode) {
                    _appHealth.value = AppHealth.UpdateRequired(
                        current = appVersionName,
                        server = configSource.getAppVersionName()
                    )
                } else {
                    _appHealth.value = AppHealth.Ready(appVersionName)
                }
            } catch (e: Exception) {
                _appHealth.value = AppHealth.Error("Error de validación")
            }
        }
    }

}