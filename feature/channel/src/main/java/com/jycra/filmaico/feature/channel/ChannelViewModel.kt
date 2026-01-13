package com.jycra.filmaico.feature.channel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jycra.filmaico.domain.channel.usecase.GetChannelContentUseCase
import com.jycra.filmaico.core.ui.util.focus.BrowseFocusState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChannelViewModel @Inject constructor(
    private val getChannelContentUseCase: GetChannelContentUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<ChannelUiState>(ChannelUiState.Loading)
    val uiState = _uiState.asStateFlow()

    private val _effect = Channel<ChannelUiEffect>()
    val effect = _effect.receiveAsFlow()

    var browseFocusState by mutableStateOf(BrowseFocusState())
        private set

    init {
        getChannelContent()
    }

    private fun getChannelContent() {
        viewModelScope.launch {
            getChannelContentUseCase()
                .onStart { _uiState.value = ChannelUiState.Loading }
                .catch { e -> _uiState.value = ChannelUiState.Error(e.message ?: "Ocurrió un error") }
                .collect { carousels ->
                    _uiState.value = ChannelUiState.Success(carousels)
                }
        }
    }

    fun onEvent(event: ChannelUiEvent) {
        when (event) {
            is ChannelUiEvent.OnChannelClick -> {
                browseFocusState = browseFocusState.copy(
                    lastFocusedCarouselIndex = event.carouselIndex,
                    lastFocusedContentIndex = event.channelIndex,
                    shouldRestoreFocus = false
                )
                viewModelScope.launch {
                    _effect.send(
                        ChannelUiEffect.NavigateToPlayer(event.channelId)
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