package com.jycra.filmaico.feature.channel

import com.jycra.filmaico.core.ui.feature.media.model.UiMediaCarousel

sealed interface ChannelUiState {
    data object Loading : ChannelUiState
    data class Success(val carousels: List<UiMediaCarousel>) : ChannelUiState
    data class Error(val message: String) : ChannelUiState
}

sealed interface ChannelUiEvent {
    data class PlayAsset(
        val assetId: String,
        val carouselIndex: Int = 0,
        val contentIndex: Int = 0
    ) : ChannelUiEvent
}

sealed interface ChannelUiEffect {
    data class PlayAsset(val assetId: String) : ChannelUiEffect
}