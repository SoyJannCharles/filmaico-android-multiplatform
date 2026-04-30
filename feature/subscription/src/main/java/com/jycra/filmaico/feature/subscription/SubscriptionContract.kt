package com.jycra.filmaico.feature.subscription

import com.jycra.filmaico.domain.user.error.AuthError

data class SubscriptionUiState(
    val isLoading: Boolean = false,
    val isDeleting: Boolean = false,
    val showDeleteConfirmation: Boolean = false,
    val showReauthDialog: Boolean = false,
    val error: AuthError? = null
)

sealed interface SubscriptionUiEvent {

    data object OnCancelRequested : SubscriptionUiEvent
    data object OnDeleteConfirmed : SubscriptionUiEvent
    data object OnDeleteDismissed : SubscriptionUiEvent

    data class OnReauthConfirmed(val password: String) : SubscriptionUiEvent
    data object OnReauthDismissed : SubscriptionUiEvent

    data object OnErrorDismiss : SubscriptionUiEvent

}

sealed interface SubscriptionUiEffect {
    object NavigateToAuth : SubscriptionUiEffect
}