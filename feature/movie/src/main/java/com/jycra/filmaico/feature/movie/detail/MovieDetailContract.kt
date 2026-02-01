package com.jycra.filmaico.feature.movie.detail

import com.jycra.filmaico.domain.media.model.Media

sealed interface MovieDetailUiState {
    data object Loading : MovieDetailUiState
    data class Success(val media: Media.Container) : MovieDetailUiState
    data class Error(val message: String) : MovieDetailUiState
}

sealed interface MovieDetailUiEvent {
    data object PlayAsset : MovieDetailUiEvent
    data object OnBackPressed : MovieDetailUiEvent
}

sealed interface MovieDetailUiEffect {
    data class NavigateToPlayer(val mediaId: String) : MovieDetailUiEffect
    data object NavigateBack : MovieDetailUiEffect
}