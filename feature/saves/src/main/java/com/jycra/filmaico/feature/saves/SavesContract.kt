package com.jycra.filmaico.feature.saves

import com.jycra.filmaico.core.ui.feature.media.model.UiMediaCarousel
import com.jycra.filmaico.domain.media.model.MediaType

sealed class SavesUiState {
    object Loading : SavesUiState()
    data class Success(
        val carousels: List<UiMediaCarousel>,
        val isEmpty: Boolean = false
    ) : SavesUiState()
    data class Error(val message: String) : SavesUiState()
}

sealed class SavesUiEvent {
    data class OpenDetail(
        val containerId: String,
        val mediaType: MediaType,
        val carouselIndex: Int,
        val contentIndex: Int
    ) : SavesUiEvent()
    data class PlayAsset(
        val assetId: String,
        val mediaType: MediaType,
        val carouselIndex: Int,
        val contentIndex: Int
    ) : SavesUiEvent()
}

sealed class SavesUiEffect {
    data class OpenDetail(val mediaType: MediaType, val containerId: String) : SavesUiEffect()
    data class PlayAsset(val mediaType: MediaType, val assetId: String) : SavesUiEffect()
}