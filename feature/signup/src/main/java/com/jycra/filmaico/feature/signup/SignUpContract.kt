package com.jycra.filmaico.feature.signup

import com.jycra.filmaico.domain.user.error.AuthError

data class SignUpUiState(
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val error: AuthError? = null
)

sealed interface SignUpUiEvent {
    data class OnEmailChange(val email: String) : SignUpUiEvent
    data class OnPasswordChange(val password: String) : SignUpUiEvent
    data object OnSignUpClick : SignUpUiEvent
    data object OnSignInClick : SignUpUiEvent
}

sealed interface SignUpUiEffect {
    data object NavigateToPay : SignUpUiEffect
    data object NavigateToSignIn : SignUpUiEffect
}