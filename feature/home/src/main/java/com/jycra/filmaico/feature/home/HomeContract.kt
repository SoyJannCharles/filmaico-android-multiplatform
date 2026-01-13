package com.jycra.filmaico.feature.home

sealed interface HomeUiState {
    data object Loading : HomeUiState
    data class Success(
        val subscriptionDaysRemaining: Int
    ) : HomeUiState
    data class Error(val message: String) : HomeUiState
}

sealed interface HomeUiEvent {
    data class OnAnimeClick(val animeId: String) : HomeUiEvent
    data class OnSerieClick(val serieId: String) : HomeUiEvent
    data class OnMovieClick(val movieId: String) : HomeUiEvent
    data class OnChannelClick(val channelId: String) : HomeUiEvent
    data object OnProfileClick : HomeUiEvent
}

sealed interface HomeUiEffect {
    data class NavigateToDetail(val contentType: String, val contentId: String) : HomeUiEffect
    data class NavigateToPlayer(val contentType: String, val contentId: String) : HomeUiEffect
    data object NavigateToProfile : HomeUiEffect
}