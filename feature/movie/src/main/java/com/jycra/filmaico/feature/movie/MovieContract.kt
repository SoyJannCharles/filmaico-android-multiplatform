package com.jycra.filmaico.feature.movie

import com.jycra.filmaico.core.ui.feature.media.model.UiMediaCarousel
import com.jycra.filmaico.domain.media.model.MediaType

sealed interface MovieUiState {
    data object Loading : MovieUiState
    data class Success(val carousels: List<UiMediaCarousel>) : MovieUiState
    data class Error(val message: String) : MovieUiState
}

sealed interface MovieUiEvent {
    data class OpenDetail(
        val containerId: String,
        val carouselIndex: Int,
        val contentIndex: Int
    ) : MovieUiEvent
    data class PlayAsset(
        val assetId: String,
        val mediaType: MediaType,
        val carouselIndex: Int,
        val contentIndex: Int
    ) : MovieUiEvent
}

sealed interface MovieUiEffect {
    data class OpenDetail(val containerId: String) : MovieUiEffect
    data class PlayAsset(val mediaType: MediaType, val assetId: String) : MovieUiEffect
}