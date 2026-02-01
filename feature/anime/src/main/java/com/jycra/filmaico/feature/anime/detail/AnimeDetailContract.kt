package com.jycra.filmaico.feature.anime.detail

import com.jycra.filmaico.core.ui.feature.media.model.UiMediaDetail
import com.jycra.filmaico.domain.media.model.MediaType

sealed interface AnimeDetailUiState {
    data object Loading : AnimeDetailUiState
    data class Success(val detail: UiMediaDetail) : AnimeDetailUiState
    data class Error(val message: String) : AnimeDetailUiState
}

sealed interface AnimeDetailUiEvent {
    data class OnSeasonSelected(val seasonId: String) : AnimeDetailUiEvent
    data class PlayAsset(
        val mediaType: MediaType,
        val assetId: String,
        val index: Int = 0
    ) : AnimeDetailUiEvent
    data object OnBackPressed : AnimeDetailUiEvent
}

sealed interface AnimeDetailUiEffect {
    data class PlayAsset(val mediaType: MediaType, val assetId: String) : AnimeDetailUiEffect
    data object NavigateBack : AnimeDetailUiEffect
}