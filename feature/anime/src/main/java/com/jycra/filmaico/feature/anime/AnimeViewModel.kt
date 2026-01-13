package com.jycra.filmaico.feature.anime

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jycra.filmaico.core.ui.util.focus.BrowseFocusState
import com.jycra.filmaico.domain.anime.usecase.GetAnimeContentUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AnimeViewModel @Inject constructor(
    private val getAnimeContentUseCase: GetAnimeContentUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<AnimeUiState>(AnimeUiState.Loading)
    val uiState: StateFlow<AnimeUiState> = _uiState.asStateFlow()

    private val _effect = Channel<AnimeUiEffect>()
    val effect = _effect.receiveAsFlow()

    var browseFocusState by mutableStateOf(BrowseFocusState())
        private set

    init {
        getAnimeContent()
    }

    private fun getAnimeContent() {
        viewModelScope.launch {
            getAnimeContentUseCase()
                .onStart { _uiState.value = AnimeUiState.Loading }
                .catch { e -> _uiState.value = AnimeUiState.Error(e.message ?: "Ocurrió un error") }
                .collect { carousels ->
                    _uiState.value = AnimeUiState.Success(carousels)
                }
        }
    }

    fun onEvent(event: AnimeUiEvent) {
        when (event) {
            is AnimeUiEvent.OnAnimeClick -> {
                browseFocusState = browseFocusState.copy(
                    lastFocusedCarouselIndex = event.carouselIndex,
                    lastFocusedContentIndex = event.animeIndex,
                    shouldRestoreFocus = false
                )
                viewModelScope.launch {
                    _effect.send(
                        AnimeUiEffect.NavigateToPlayer(event.animeId)
                    )
                }
            }
        }
    }

    fun onScreenResumed() {
        browseFocusState = browseFocusState.copy(
            shouldRestoreFocus = true
        )
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