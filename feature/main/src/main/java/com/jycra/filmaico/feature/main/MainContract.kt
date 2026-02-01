package com.jycra.filmaico.feature.main

import com.jycra.filmaico.core.ui.util.UiText

sealed interface MainUiState {
    data object Loading: MainUiState
    data class Success(
        val expirationTimestamp: Long? = null,
        val expirationText: UiText = UiText.Empty,
        val isSubscriptionUrgent: Boolean = false,
    ): MainUiState
    data class Error(val message: String): MainUiState
}