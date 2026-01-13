package com.jycra.filmaico.feature.splash

sealed interface SplashUiState {
    data object Loading : SplashUiState
    data object NoNetwork : SplashUiState
    data class UpdateRequired(val oldVersion: String, val newVersion: String) : SplashUiState
}

sealed interface SplashUiEffect {
    data object NavigateToSignIn : SplashUiEffect
    data object NavigateToPay : SplashUiEffect
    data object NavigateToHome : SplashUiEffect
}