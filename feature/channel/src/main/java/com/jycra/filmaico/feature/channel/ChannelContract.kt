package com.jycra.filmaico.feature.channel

import com.jycra.filmaico.domain.channel.model.ChannelCarousel

sealed interface ChannelUiState {
    data object Loading : ChannelUiState
    data class Success(val carousels: List<ChannelCarousel>) : ChannelUiState
    data class Error(val message: String) : ChannelUiState
}

sealed interface ChannelUiEvent {
    data class OnChannelClick(
        val channelId: String,
        val carouselIndex: Int = 0,
        val channelIndex: Int = 0
    ) : ChannelUiEvent
}

sealed interface ChannelUiEffect {
    data class NavigateToPlayer(val channelId: String) : ChannelUiEffect
}