package com.jycra.filmaico.feature.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jycra.filmaico.core.config.ConfigSource
import com.jycra.filmaico.core.network.ConnectivityObserver
import com.jycra.filmaico.domain.user.usecase.GetCurrentUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val configSource: ConfigSource,
    private val connectivityObserver: ConnectivityObserver,
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val appVersionCode: Long,
    private val appVersionName: String
) : ViewModel() {

    private val _uiState = MutableStateFlow<SplashUiState>(SplashUiState.Loading)
    val uiState = _uiState.asStateFlow()

    private val _effect = Channel<SplashUiEffect>()
    val effect = _effect.receiveAsFlow()

    init {
        startAppChecks()
    }

    private fun startAppChecks() {

        viewModelScope.launch {

            connectivityObserver.observe().collectLatest { networkStatus ->
                when (networkStatus) {
                    ConnectivityObserver.Status.Available -> {
                        _uiState.value = SplashUiState.Loading
                        runChecks()
                    }
                    else -> {
                        _uiState.value = SplashUiState.NoNetwork
                    }
                }
            }

        }

    }

    private suspend fun runChecks() {

        val isUpdateRequired = appVersionCode < configSource.getAppVersionCode()

        if (isUpdateRequired) {
            _uiState.value = SplashUiState.UpdateRequired(
                oldVersion = appVersionName,
                newVersion = configSource.getAppVersionName()
            )
            return
        }

        checkUserSession()

    }

    private suspend fun checkUserSession() {

        val user = getCurrentUserUseCase()

        if (user == null) {
            _effect.send(SplashUiEffect.NavigateToSignIn)
        } else {
            if (user.subscription.isActive()) {
                _effect.send(SplashUiEffect.NavigateToHome)
            } else {
                _effect.send(SplashUiEffect.NavigateToPay)
            }
        }

    }

}