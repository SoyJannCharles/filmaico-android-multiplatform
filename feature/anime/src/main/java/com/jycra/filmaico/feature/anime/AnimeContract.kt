package com.jycra.filmaico.feature.anime

import com.jycra.filmaico.domain.anime.model.AnimeCarousel

sealed interface AnimeUiState {
    object Loading : AnimeUiState
    data class Success(val carousels: List<AnimeCarousel>) : AnimeUiState
    data class Error(val message: String) : AnimeUiState
}

sealed interface AnimeUiEvent {
    data class OnAnimeClick(
        val animeId: String,
        val carouselIndex: Int? = null,
        val animeIndex: Int? = null
    ) : AnimeUiEvent
}

sealed interface AnimeUiEffect {
    data class NavigateToPlayer(val animeId: String) : AnimeUiEffect
}