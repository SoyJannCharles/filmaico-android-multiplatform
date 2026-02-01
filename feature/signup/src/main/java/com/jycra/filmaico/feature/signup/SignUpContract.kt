package com.jycra.filmaico.feature.signup

import com.jycra.filmaico.domain.user.error.AuthError

data class SignUpUiState(
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val error: AuthError? = null
)

sealed interface SignUpUiEvent {
    data class EmailChange(val email: String) : SignUpUiEvent
    data class PasswordChange(val password: String) : SignUpUiEvent
    data object SignUpTriggered : SignUpUiEvent
    data object SignInTriggered : SignUpUiEvent
}

sealed interface SignUpUiEffect {
    data object NavigateToSubscription : SignUpUiEffect
    data object NavigateToAuth : SignUpUiEffect
}