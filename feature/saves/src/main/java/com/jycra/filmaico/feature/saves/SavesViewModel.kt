package com.jycra.filmaico.feature.saves

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jycra.filmaico.core.ui.feature.media.util.mapper.toUiCarousels
import com.jycra.filmaico.core.ui.util.focus.MediaFocusState
import com.jycra.filmaico.domain.media.usecase.GetSavedMediaUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SavesViewModel @Inject constructor(
    private val getSavedMediaUseCase: GetSavedMediaUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<SavesUiState>(SavesUiState.Loading)
    val uiState = _uiState.asStateFlow()

    private val _effect = Channel<SavesUiEffect>()
    val effect = _effect.receiveAsFlow()

    var mediaFocusState by mutableStateOf(MediaFocusState())
        private set

    init {
        loadSavedMedia()
    }

    fun loadSavedMedia() {
        viewModelScope.launch {
            getSavedMediaUseCase()
                .onStart { _uiState.value = SavesUiState.Loading }
                .onEach { carousels ->
                    _uiState.value = SavesUiState.Success(carousels.map { it.toUiCarousels() })
                }
                .catch { _uiState.value = SavesUiState.Error("Error al cargar") }
                .launchIn(viewModelScope)
        }
    }

    fun onEvent(event: SavesUiEvent) {
        when (event) {
            is SavesUiEvent.OpenDetail -> {
                mediaFocusState = mediaFocusState.copy(
                    lastFocusedCarouselIndex = event.carouselIndex,
                    lastFocusedContentIndex = event.contentIndex,
                    shouldRestoreFocus = false
                )
                viewModelScope.launch {
                    _effect.send(SavesUiEffect.OpenDetail(
                        mediaType = event.mediaType,
                        containerId = event.containerId
                    ))
                }
            }
            is SavesUiEvent.PlayAsset -> {
                mediaFocusState = mediaFocusState.copy(
                    lastFocusedCarouselIndex = event.carouselIndex,
                    lastFocusedContentIndex = event.contentIndex,
                    shouldRestoreFocus = false
                )
                viewModelScope.launch {
                    _effect.send(SavesUiEffect.PlayAsset(
                        mediaType = event.mediaType,
                        assetId = event.assetId
                    ))
                }
            }
        }
    }

    fun onScreenResumed() {
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
        mediaFocusState = mediaFocusState.copy(shouldRestoreFocus = false)
    }

}