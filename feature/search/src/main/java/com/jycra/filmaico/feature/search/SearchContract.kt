package com.jycra.filmaico.feature.search

import com.jycra.filmaico.core.ui.feature.media.model.UiMediaCarousel
import com.jycra.filmaico.domain.media.model.MediaType

data class SearchUiState(
    val isLoading: Boolean = false,
    val searchQuery: String = "",
    val results: List<UiMediaCarousel> = emptyList(),
    val error: String? = null
)

sealed interface SearchUiEvent {
    data class OnQueryChange(val query: String) : SearchUiEvent
    data class OpenDetail(
        val containerId: String,
        val mediaType: MediaType,
        val carouselId: String,
        val carouselIndex: Int = 0,
        val contentIndex: Int = 0
    ) : SearchUiEvent
    data class PlayAsset(
        val assetId: String,
        val mediaType: MediaType,
        val carouselId: String,
        val carouselIndex: Int = 0,
        val contentIndex: Int = 0
    ) : SearchUiEvent
}

sealed interface SearchUiEffect {
    data class OpenDetail(val mediaType: MediaType, val containerId: String) : SearchUiEffect
    data class PlayAsset(val mediaType: MediaType, val assetId: String) : SearchUiEffect
}