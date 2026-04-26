package com.jycra.filmaico.feature.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jycra.filmaico.core.config.ConfigSource
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
    private val appVersionCode: Long,
    private val appVersionName: String
) : ViewModel() {

    private val _uiState = MutableStateFlow<SplashUiState>(SplashUiState.Cheking)
    val uiState = _uiState.asStateFlow()

    private val _effect = Channel<SplashUiEffect>()
    val effect = _effect.receiveAsFlow()

    init {
        checkAppHealth()
    }

    private fun checkAppHealth() {
        viewModelScope.launch {
            try {
                val serverVersionCode = configSource.getAppVersionCode()
                if (appVersionCode < serverVersionCode) {
                    _uiState.value = SplashUiState.UpdateRequired(
                        currentVersion = appVersionName,
                        serverVersion = configSource.getAppVersionName()
                    )
                } else {
                    _uiState.value = SplashUiState.ReadyToCheckSession
                }
            } catch (e: Exception) {
                _uiState.value = SplashUiState.Error("Error de conexión")
            }
        }
    }

}