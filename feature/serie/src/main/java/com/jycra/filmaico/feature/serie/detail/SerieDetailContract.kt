package com.jycra.filmaico.feature.serie.detail

import com.jycra.filmaico.core.ui.feature.media.model.UiMediaDetail
import com.jycra.filmaico.domain.media.model.MediaType


sealed interface SerieDetailUiState {
    data object Loading : SerieDetailUiState
    data class Success(val detail: UiMediaDetail) : SerieDetailUiState
    data class Error(val message: String) : SerieDetailUiState
}

sealed interface SerieDetailUiEvent {
    data class OnSeasonSelected(val seasonId: String) : SerieDetailUiEvent
    data class PlayAsset(
        val mediaType: MediaType,
        val assetId: String,
        val index: Int = 0
    ) : SerieDetailUiEvent
    data object OnBackPressed : SerieDetailUiEvent
}

sealed interface SerieDetailUiEffect {
    data class PlayAsset(val mediaType: MediaType, val assetId: String) : SerieDetailUiEffect
    data object NavigateBack : SerieDetailUiEffect
}