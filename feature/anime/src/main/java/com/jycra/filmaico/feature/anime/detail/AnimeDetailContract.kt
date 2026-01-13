package com.jycra.filmaico.feature.anime.detail

import com.jycra.filmaico.domain.anime.model.Anime
import com.jycra.filmaico.domain.anime.model.AnimeContent
import com.jycra.filmaico.domain.anime.model.AnimeSeason

sealed interface AnimeDetailUiState {
    data object Loading : AnimeDetailUiState
    data class Success(
        val anime: Anime,
        val selectedSeason: AnimeSeason,
        val contentsForSeason: List<AnimeContent>
    ) : AnimeDetailUiState
    data class Error(val message: String) : AnimeDetailUiState
}

sealed interface AnimeDetailUiEvent {
    data class OnSeasonSelected(val season: AnimeSeason) : AnimeDetailUiEvent
    data class OnContentClick(
        val content: AnimeContent,
        val index: Int = 0
    ) : AnimeDetailUiEvent
    object OnBackPressed : AnimeDetailUiEvent
}

sealed interface AnimeDetailUiEffect {
    data class NavigateToPlayer(val animeId: String) : AnimeDetailUiEffect
    object NavigateBack : AnimeDetailUiEffect
}