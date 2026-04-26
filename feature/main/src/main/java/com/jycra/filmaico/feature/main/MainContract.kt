package com.jycra.filmaico.feature.main

import com.jycra.filmaico.core.ui.util.UiText

sealed interface MainUiState {
    data object Idle: MainUiState
    data object Loading: MainUiState
    data class Ready(
        val expirationTimestamp: Long? = null,
        val expirationText: UiText = UiText.Empty,
        val isSubscriptionUrgent: Boolean = false
    ): MainUiState
    data class Error(val message: String): MainUiState
}

sealed interface SyncStatus {
    data object Loading : SyncStatus
    data object Success : SyncStatus
    data class Error(val message: String) : SyncStatus
}