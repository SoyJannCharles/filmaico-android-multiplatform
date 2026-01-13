package com.jycra.filmaico.feature.movie

import com.jycra.filmaico.domain.movie.model.MovieCarousel

sealed interface MovieUiState {
    object Loading : MovieUiState
    data class Success(val carousels: List<MovieCarousel>) : MovieUiState
    data class Error(val message: String) : MovieUiState
}

sealed interface MovieUiEvent {
    data class OnMovieClick(
        val movieId: String,
        val carouselIndex: Int? = null,
        val movieIndex: Int? = null
    ) : MovieUiEvent
}

sealed interface MovieUiEffect {
    data class NavigateToDetail(val movieId: String) : MovieUiEffect
}