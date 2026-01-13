package com.jycra.filmaico.feature.search.model

import com.jycra.filmaico.core.ui.feature.search.UiSearchCarousel

sealed interface SearchResult {
    data object Loading : SearchResult
    data class Success(val carousels: List<UiSearchCarousel>) : SearchResult
    data object Empty : SearchResult
}