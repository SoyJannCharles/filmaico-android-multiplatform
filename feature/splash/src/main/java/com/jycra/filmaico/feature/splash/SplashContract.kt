package com.jycra.filmaico.feature.splash

sealed interface SplashUiState {
    data object Cheking : SplashUiState
    data class UpdateRequired(val currentVersion: String, val serverVersion: String) : SplashUiState
    data object ReadyToCheckSession : SplashUiState
    data class Error(val message: String) : SplashUiState
}

sealed interface SplashUiEffect {
    data object NavigateToAuth : SplashUiEffect
    data object NavigateToSubscription : SplashUiEffect
    data object NavigateToMain : SplashUiEffect
}