package com.jycra.filmaico.feature.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jycra.filmaico.core.config.ConfigSource
import com.jycra.filmaico.domain.user.usecase.GetCurrentUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val configSource: ConfigSource,
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val appVersionCode: Long,
    private val appVersionName: String
) : ViewModel() {

    private val _uiState = MutableStateFlow<SplashUiState>(SplashUiState.Cheking)
    val uiState = _uiState.asStateFlow()

    private val _effect = Channel<SplashUiEffect>()
    val effect = _effect.receiveAsFlow()

    init {
        initializeApplication()
    }

    private fun initializeApplication() {

        viewModelScope.launch {

            try {
                validateAppVersion()
            } catch (e: Exception) {
                _uiState.value = SplashUiState.Error("Error al conectar con el servidor")
            }

        }

    }

    private suspend fun validateAppVersion() {

        val serverVersionCode = configSource.getAppVersionCode()
        val isUpdateRequired = appVersionCode < serverVersionCode

        if (isUpdateRequired) {
            _uiState.value = SplashUiState.UpdateRequired(
                currentVersion = appVersionName,
                serverVersion = configSource.getAppVersionName()
            )
            return
        }

        validateUserSession()

    }

    private suspend fun validateUserSession() {

        val user = getCurrentUserUseCase()

        val effect = when {
            user == null -> SplashUiEffect.NavigateToAuth
            user.subscription.isActive() -> SplashUiEffect.NavigateToMain
            else -> SplashUiEffect.NavigateToSubscription
        }

        _effect.send(effect)

    }

}