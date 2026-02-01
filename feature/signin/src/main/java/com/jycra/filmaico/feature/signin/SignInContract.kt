package com.jycra.filmaico.feature.signin

import com.jycra.filmaico.domain.user.error.AuthError

data class SignInUiState(
    val email: String = "",
    val password: String = "",
    val isFormValid: Boolean = false,
    val isLoading: Boolean = false,
    val error: AuthError? = null
)

sealed interface SignInUiEvent {
    data class EmailChanged(val email: String) : SignInUiEvent
    data class PasswordChanged(val password: String) : SignInUiEvent
    data object SignInTriggered : SignInUiEvent
    data object SignUpTriggered : SignInUiEvent
}

sealed interface SignInUiEffect {
    data object NavigateToSignUp : SignInUiEffect
    data object NavigateToSubscription : SignInUiEffect
    data object NavigateToMain : SignInUiEffect
}