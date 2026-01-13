package com.jycra.filmaico.feature.search

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jycra.filmaico.core.navigation.ContentType
import com.jycra.filmaico.core.ui.util.focus.BrowseFocusState
import com.jycra.filmaico.domain.search.usecase.SearchUseCase
import com.jycra.filmaico.feature.search.model.SearchResult
import com.jycra.filmaico.feature.search.util.toUiCarousels
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
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
    private val searchUseCase: SearchUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState = _uiState.asStateFlow()

    private val _effect = Channel<SearchUiEffect>()
    val effect = _effect.receiveAsFlow()

    var browseFocusState by mutableStateOf(BrowseFocusState())
        private set

    init {
        viewModelScope.launch {
            _uiState
                .map { it.searchQuery }
                .distinctUntilChanged()
                .debounce(2000L)
                .flatMapLatest { query ->
                    if (query.isBlank()) {
                        flowOf(SearchResult.Empty)
                    } else {
                        flow<SearchResult> {
                            emit(SearchResult.Loading)
                            val carousels = searchUseCase(query)
                                .first()
                                .toUiCarousels()
                            emit(SearchResult.Success(carousels))
                        }
                    }
                }
                .collect { result ->
                    _uiState.update {
                        when (result) {
                            is SearchResult.Loading -> it.copy(isLoading = true)
                            is SearchResult.Success -> it.copy(
                                carousels = result.carousels,
                                isLoading = false
                            )
                            is SearchResult.Empty -> it.copy(
                                carousels = emptyList(),
                                isLoading = false
                            )
                        }
                    }

                    // Resetear foco cuando llegan nuevos resultados
                    if (result is SearchResult.Success && result.carousels.isNotEmpty()) {
                        browseFocusState = browseFocusState.copy(
                            lastFocusedCarouselIndex = 0,
                            lastFocusedContentIndex = 0,
                            hasConsumedInitialFocus = true,
                            shouldRestoreFocus = false
                        )
                    }
                }
        }
    }

    fun onEvent(event: SearchUiEvent) {
        when (event) {
            is SearchUiEvent.OnQueryChange -> handleQueryChange(event.query)
            is SearchUiEvent.OnChannelClick -> handleContentClick(
                ContentType.CHANNEL,
                event.channelId,
                event.carouselIndex,
                event.contentIndex
            )

            is SearchUiEvent.OnMovieClick -> handleContentClick(
                ContentType.MOVIE,
                event.movieId,
                event.carouselIndex,
                event.contentIndex
            )

            is SearchUiEvent.OnSerieClick -> handleContentClick(
                ContentType.SERIE,
                event.serieId,
                event.carouselIndex,
                event.contentIndex
            )

            is SearchUiEvent.OnAnimeClick -> handleContentClick(
                ContentType.ANIME,
                event.animeId,
                event.carouselIndex,
                event.contentIndex
            )
        }
    }

    private fun handleQueryChange(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
    }

    private fun handleContentClick(
        contentType: String,
        contentId: String,
        carouselIndex: Int,
        contentIndex: Int
    ) {
        viewModelScope.launch {
            saveFocusPosition(carouselIndex, contentIndex)
            if (contentType == ContentType.CHANNEL) {
                _effect.send(
                    SearchUiEffect.NavigateToPlayer(
                        contentType = contentType,
                        contentId = contentId,
                    )
                )
            } else {
                _effect.send(
                    SearchUiEffect.NavigateToDetail(
                        contentType = contentType,
                        contentId = contentId
                    )
                )
            }
        }
    }

    fun onScreenResumed() {
        requestInitialBrowseFocus()
    }

    fun requestInitialBrowseFocus() {
        browseFocusState = browseFocusState.copy(
            shouldRestoreFocus = true
        )
        Log.d("SearchViewModel", "Requesting initial focus: $browseFocusState")
    }

    fun saveFocusPosition(carouselIndex: Int, contentIndex: Int) {
        browseFocusState = browseFocusState.copy(
            lastFocusedCarouselIndex = carouselIndex,
            lastFocusedContentIndex = contentIndex
        )
    }

    fun markInitialFocusConsumed() {
        browseFocusState = browseFocusState.copy(
            lastFocusedCarouselIndex = 0,
            lastFocusedContentIndex = 0,
            hasConsumedInitialFocus = true,
            shouldRestoreFocus = false
        )
    }

    fun markFocusRestored() {
        browseFocusState = browseFocusState.copy(shouldRestoreFocus = false)
    }

}