package com.jycra.filmaico.feature.movie.detail

import com.jycra.filmaico.domain.movie.model.Movie

sealed interface MovieDetailUiState {
    data object Loading : MovieDetailUiState
    data class Success(val movie: Movie) : MovieDetailUiState
    data class Error(val message: String) : MovieDetailUiState
}

sealed interface MovieDetailUiEvent {
    data class OnStartPlayback(val movie: Movie) : MovieDetailUiEvent
    data object OnBackPressed : MovieDetailUiEvent
}

sealed interface MovieDetailUiEffect {
    data class NavigateToPlayer(val movieId: String) : MovieDetailUiEffect
    data object NavigateBack : MovieDetailUiEffect
}