package com.jycra.filmaico.feature.movie

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jycra.filmaico.core.ui.util.focus.BrowseFocusState
import com.jycra.filmaico.domain.movie.usecase.GetMovieContentUseCase
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
class MovieViewModel @Inject constructor(
    private val getMovieContentUseCase: GetMovieContentUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<MovieUiState>(MovieUiState.Loading)
    val uiState: StateFlow<MovieUiState> = _uiState.asStateFlow()

    private val _effect = Channel<MovieUiEffect>()
    val effect = _effect.receiveAsFlow()

    var browseFocusState by mutableStateOf(BrowseFocusState())
        private set

    init {
        getMovieContent()
    }

    private fun getMovieContent() {
        viewModelScope.launch {
            getMovieContentUseCase()
                .onStart { _uiState.value = MovieUiState.Loading }
                .catch { e -> _uiState.value = MovieUiState.Error(e.message ?: "Ocurrió un error") }
                .collect { carousels ->
                    _uiState.value = MovieUiState.Success(carousels)
                }
        }
    }

    fun onEvent(event: MovieUiEvent) {
        when (event) {
            is MovieUiEvent.OnMovieClick -> {
                browseFocusState = browseFocusState.copy(
                    lastFocusedCarouselIndex = event.carouselIndex,
                    lastFocusedContentIndex = event.movieIndex,
                    shouldRestoreFocus = false
                )
                viewModelScope.launch {
                    _effect.send(
                        MovieUiEffect.NavigateToDetail(event.movieId)
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