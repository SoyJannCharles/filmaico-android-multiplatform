package com.jycra.filmaico.feature.search.model

import com.jycra.filmaico.core.ui.feature.media.model.UiMediaCarousel

sealed interface SearchResult {
    data object Loading : SearchResult
    data class Success(val carousels: List<UiMediaCarousel>) : SearchResult
    data object Empty : SearchResult
}