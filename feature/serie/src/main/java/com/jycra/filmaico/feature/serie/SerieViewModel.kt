package com.jycra.filmaico.feature.serie

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jycra.filmaico.core.ui.util.focus.BrowseFocusState
import com.jycra.filmaico.domain.serie.usecase.GetSerieContentUseCase
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
class SerieViewModel @Inject constructor(
    private val getSerieContentUseCase: GetSerieContentUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<SerieUiState>(SerieUiState.Loading)
    val uiState: StateFlow<SerieUiState> = _uiState.asStateFlow()

    private val _effect = Channel<SerieUiEffect>()
    val effect = _effect.receiveAsFlow()

    var browseFocusState by mutableStateOf(BrowseFocusState())
        private set

    init {
        getSerieContent()
    }

    private fun getSerieContent() {
        viewModelScope.launch {
            getSerieContentUseCase()
                .onStart { _uiState.value = SerieUiState.Loading }
                .catch { e -> _uiState.value = SerieUiState.Error(e.message ?: "Ocurrió un error") }
                .collect { carousels ->
                    _uiState.value = SerieUiState.Success(carousels)
                }
        }
    }

    fun onEvent(event: SerieUiEvent) {
        when (event) {
            is SerieUiEvent.OnSerieClick -> {
                browseFocusState = browseFocusState.copy(
                    lastFocusedCarouselIndex = event.carouselIndex,
                    lastFocusedContentIndex = event.serieIndex,
                    shouldRestoreFocus = false
                )
                viewModelScope.launch {
                    _effect.send(
                        SerieUiEffect.NavigateToDetail(event.serieId)
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