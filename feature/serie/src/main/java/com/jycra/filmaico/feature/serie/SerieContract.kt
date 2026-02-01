package com.jycra.filmaico.feature.serie

import com.jycra.filmaico.core.ui.feature.media.model.UiMediaCarousel
import com.jycra.filmaico.domain.media.model.MediaType

sealed interface SerieUiState {
    object Loading : SerieUiState
    data class Success(val carousels: List<UiMediaCarousel>) : SerieUiState
    data class Error(val message: String) : SerieUiState
}

sealed interface SerieUiEvent {
    data class OpenDetail(
        val containerId: String,
        val carouselIndex: Int,
        val contentIndex: Int
    ) : SerieUiEvent
    data class PlayAsset(
        val assetId: String,
        val mediaType: MediaType,
        val carouselIndex: Int,
        val contentIndex: Int
    ) : SerieUiEvent
}

sealed interface SerieUiEffect {
    data class OpenDetail(val containerId: String) : SerieUiEffect
    data class PlayAsset(val mediaType: MediaType, val assetId: String) : SerieUiEffect
}