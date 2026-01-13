package com.jycra.filmaico.feature.serie.detail

import com.jycra.filmaico.domain.serie.model.Serie
import com.jycra.filmaico.domain.serie.model.SerieContent
import com.jycra.filmaico.domain.serie.model.SerieSeason

sealed interface SerieDetailUiState {
    data object Loading : SerieDetailUiState
    data class Success(
        val serie: Serie,
        val selectedSeason: SerieSeason,
        val contentsForSeason: List<SerieContent>
    ) : SerieDetailUiState
    data class Error(val message: String) : SerieDetailUiState
}

sealed interface SerieDetailUiEvent {
    data class OnSeasonSelected(val season: SerieSeason) : SerieDetailUiEvent
    data class OnContentClick(
        val content: SerieContent,
        val index: Int = 0
    ) : SerieDetailUiEvent
    data object OnBackPressed : SerieDetailUiEvent
}

sealed interface SerieDetailUiEffect {
    data class NavigateToPlayer(val serieId: String) : SerieDetailUiEffect
    data object NavigateBack : SerieDetailUiEffect
}