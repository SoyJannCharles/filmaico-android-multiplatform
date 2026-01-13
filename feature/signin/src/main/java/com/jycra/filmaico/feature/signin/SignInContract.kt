package com.jycra.filmaico.feature.signin

import com.jycra.filmaico.domain.user.error.AuthError

data class SignInUiState(
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val error: AuthError? = null
)

sealed interface SignInUiEvent {
    data class OnEmailChange(val email: String) : SignInUiEvent
    data class OnPasswordChange(val password: String) : SignInUiEvent
    data object OnSignInClick : SignInUiEvent
    data object OnSignUpClick : SignInUiEvent
}

sealed interface SignInUiEffect {
    data object NavigateToSignUp : SignInUiEffect
    data object NavigateToPay : SignInUiEffect
    data object NavigateToHome : SignInUiEffect
}