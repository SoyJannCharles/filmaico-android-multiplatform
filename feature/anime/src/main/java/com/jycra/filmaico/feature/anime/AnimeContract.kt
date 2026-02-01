package com.jycra.filmaico.feature.anime

import com.jycra.filmaico.core.ui.feature.media.model.UiMediaCarousel
import com.jycra.filmaico.domain.media.model.MediaType

sealed interface AnimeUiState {
    object Loading : AnimeUiState
    data class Success(val carousels: List<UiMediaCarousel>) : AnimeUiState
    data class Error(val message: String) : AnimeUiState
}

sealed interface AnimeUiEvent {
    data class OpenDetail(
        val containerId: String,
        val carouselId: String,
        val carouselIndex: Int,
        val contentIndex: Int
    ) : AnimeUiEvent
    data class PlayAsset(
        val assetId: String,
        val mediaType: MediaType,
        val carouselIndex: Int,
        val contentIndex: Int
    ) : AnimeUiEvent
}

sealed interface AnimeUiEffect {
    data class OpenDetail(val containerId: String) : AnimeUiEffect
    data class PlayAsset(val mediaType: MediaType, val assetId: String) : AnimeUiEffect
}