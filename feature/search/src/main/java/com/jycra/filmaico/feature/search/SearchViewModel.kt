package com.jycra.filmaico.feature.search

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jycra.filmaico.core.ui.feature.media.util.mapper.toUiCarousels
import com.jycra.filmaico.core.ui.util.focus.MediaFocusState
import com.jycra.filmaico.domain.media.usecase.SearchAllMediaUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchAllMediaUseCase: SearchAllMediaUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState = _uiState.asStateFlow()

    private val _effect = Channel<SearchUiEffect>()
    val effect = _effect.receiveAsFlow()

    var mediaFocusState by mutableStateOf(MediaFocusState(
        hasConsumedInitialFocus = true,
        shouldRestoreFocus = false
    ))
        private set

    init {
        viewModelScope.launch {
            uiState
                .map { it.searchQuery }
                .distinctUntilChanged()
                .debounce(500L)
                .flatMapLatest { query ->
                    if (query.isBlank()) {
                        flowOf(emptyList())
                    } else {
                        flow {
                            _uiState.update { it.copy(isLoading = true) }

                            val resultsMap = searchAllMediaUseCase(query)
                            emit(resultsMap.toUiCarousels())
                        }
                    }
                }
                .collect { carousels ->
                    _uiState.update {
                        it.copy(
                            results = carousels,
                            isLoading = false
                        )
                    }
                }
        }
    }

    fun onEvent(event: SearchUiEvent) {
        when (event) {
            is SearchUiEvent.OnQueryChange -> {
                _uiState.update { it.copy(searchQuery = event.query) }
            }
            is SearchUiEvent.OpenDetail -> {
                mediaFocusState = mediaFocusState.copy(
                    lastFocusedCarouselId = event.carouselId,
                    lastFocusedCarouselIndex = event.carouselIndex,
                    lastFocusedContentIndex = event.contentIndex,
                    shouldRestoreFocus = false
                )
                viewModelScope.launch {
                    _effect.send(SearchUiEffect.OpenDetail(
                            mediaType = event.mediaType,
                            containerId = event.containerId
                    ))
                }
            }
            is SearchUiEvent.PlayAsset -> {
                mediaFocusState = mediaFocusState.copy(
                    lastFocusedCarouselId = event.carouselId,
                    lastFocusedCarouselIndex = event.carouselIndex,
                    lastFocusedContentIndex = event.contentIndex,
                    shouldRestoreFocus = false
                )
                viewModelScope.launch {
                    _effect.send(SearchUiEffect.PlayAsset(
                        mediaType = event.mediaType,
                        assetId = event.assetId
                    ))
                }
            }
        }
    }

    fun onScreenResumed() {
        requestInitialBrowseFocus()
    }

    fun requestInitialBrowseFocus() {
        mediaFocusState = mediaFocusState.copy(
            shouldRestoreFocus = true
        )
    }

    fun saveFocusPosition(carouselIndex: Int, contentIndex: Int) {
        mediaFocusState = mediaFocusState.copy(
            lastFocusedCarouselIndex = carouselIndex,
            lastFocusedContentIndex = contentIndex
        )
    }

    fun markInitialFocusConsumed() {
        mediaFocusState = mediaFocusState.copy(
            lastFocusedCarouselIndex = 0,
            lastFocusedContentIndex = 0,
            hasConsumedInitialFocus = true,
            shouldRestoreFocus = false
        )
    }

    fun markFocusRestored() {
        mediaFocusState = mediaFocusState.copy(
            lastFocusedCarouselId = null,
            shouldRestoreFocus = false
        )
    }

}