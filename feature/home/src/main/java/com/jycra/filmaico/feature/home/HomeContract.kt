package com.jycra.filmaico.feature.home

import com.jycra.filmaico.domain.media.model.MediaType

sealed interface HomeUiState {
    data object Loading : HomeUiState
    data class Success(
        val subscriptionDaysRemaining: Int
    ) : HomeUiState
    data class Error(val message: String) : HomeUiState
}

sealed interface HomeUiEvent {
    data class OpenDetail(val mediaType: MediaType, val containerId: String) : HomeUiEvent
    data class PlayAsset(val mediaType: MediaType, val assetId: String) : HomeUiEvent
    data object OnProfileClick : HomeUiEvent
}

sealed interface HomeUiEffect {
    data class OpenDetail(val mediaType: MediaType, val containerId: String) : HomeUiEffect
    data class PlayAsset(val mediaType: MediaType, val assetId: String) : HomeUiEffect
    data object NavigateToProfile : HomeUiEffect
}