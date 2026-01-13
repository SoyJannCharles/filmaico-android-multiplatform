package com.jycra.filmaico.feature.pay

import com.jycra.filmaico.domain.user.error.AuthError

data class PayUiState(
    val showDeleteConfirmation: Boolean = false,
    val showReauthDialog: Boolean = false,
    val isDeleting: Boolean = false,
    val error: AuthError? = null
)

sealed interface PayUiEvent {

    object OnCancelClick : PayUiEvent
    data object OnConfirmDelete : PayUiEvent
    data object OnDismissDelete : PayUiEvent

    data object OnErrorDismiss : PayUiEvent

    data class OnReauthConfirm(val password: String) : PayUiEvent
    data object OnReauthDismiss : PayUiEvent

}

sealed interface PayUiEffect {
    object NavigateToSignIn : PayUiEffect
    object NavigateToHome : PayUiEffect
}