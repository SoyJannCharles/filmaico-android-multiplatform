package com.jycra.filmaico.feature.search

import com.jycra.filmaico.core.ui.feature.search.UiSearchCarousel

data class SearchUiState(
    val isLoading: Boolean = false,
    val searchQuery: String = "",
    val carousels: List<UiSearchCarousel> = emptyList()
)

sealed interface SearchUiEvent {
    data class OnQueryChange(val query: String) : SearchUiEvent
    data class OnChannelClick(
        val channelId: String,
        val carouselIndex: Int = 0,
        val contentIndex: Int = 0
    ) : SearchUiEvent
    data class OnMovieClick(
        val movieId: String,
        val carouselIndex: Int = 0,
        val contentIndex: Int = 0
    ) : SearchUiEvent
    data class OnSerieClick(
        val serieId: String,
        val carouselIndex: Int = 0,
        val contentIndex: Int = 0
    ) : SearchUiEvent
    data class OnAnimeClick(
        val animeId: String,
        val carouselIndex: Int = 0,
        val contentIndex: Int = 0
    ) : SearchUiEvent

}

sealed interface SearchUiEffect {
    data class NavigateToPlayer(val contentType: String, val contentId: String) : SearchUiEffect
    data class NavigateToDetail(val contentType: String, val contentId: String) : SearchUiEffect
}