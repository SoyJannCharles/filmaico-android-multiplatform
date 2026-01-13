package com.jycra.filmaico.feature.serie

import com.jycra.filmaico.domain.serie.model.SerieCarousel

sealed interface SerieUiState {
    object Loading : SerieUiState
    data class Success(val carousels: List<SerieCarousel>) : SerieUiState
    data class Error(val message: String) : SerieUiState
}

sealed interface SerieUiEvent {
    data class OnSerieClick(
        val serieId: String,
        val carouselIndex: Int? = null,
        val serieIndex: Int? = null
    ) : SerieUiEvent
}

sealed interface SerieUiEffect {
    data class NavigateToDetail(val serieId: String) : SerieUiEffect
}