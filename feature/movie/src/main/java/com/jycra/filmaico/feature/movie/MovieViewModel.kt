package com.jycra.filmaico.feature.movie

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jycra.filmaico.core.ui.feature.media.util.mapper.toUiCarousels
import com.jycra.filmaico.core.ui.util.focus.MediaFocusState
import com.jycra.filmaico.domain.media.model.MediaType
import com.jycra.filmaico.domain.media.usecase.GetMediaContentUseCase
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
class MovieViewModel @Inject constructor(
    private val getMediaContentUseCase: GetMediaContentUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<MovieUiState>(MovieUiState.Loading)
    val uiState = _uiState.asStateFlow()

    private val _effect = Channel<MovieUiEffect>()
    val effect = _effect.receiveAsFlow()

    var mediaFocusState by mutableStateOf(MediaFocusState())
        private set

    init {
        observeContent()
    }

    private fun observeContent() {
        viewModelScope.launch {
            getMediaContentUseCase(mediaType = MediaType.MOVIE)
                .onStart { _uiState.value = MovieUiState.Loading }
                .catch { e -> _uiState.value = MovieUiState.Error(e.message ?: "Ocurrió un error") }
                .collect { carousels ->
                    _uiState.value = MovieUiState.Success(
                        carousels = carousels.map { carousel ->
                            carousel.toUiCarousels()
                        }
                    )
                }
        }
    }

    fun onEvent(event: MovieUiEvent) {
        when (event) {
            is MovieUiEvent.OpenDetail -> {
                mediaFocusState = mediaFocusState.copy(
                    lastFocusedCarouselIndex = event.carouselIndex,
                    lastFocusedContentIndex = event.contentIndex,
                    shouldRestoreFocus = false
                )
                viewModelScope.launch {
                    _effect.send(
                        MovieUiEffect.OpenDetail(containerId = event.containerId)
                    )
                }
            }
            is MovieUiEvent.PlayAsset -> {
                mediaFocusState = mediaFocusState.copy(
                    lastFocusedCarouselIndex = event.carouselIndex,
                    lastFocusedContentIndex = event.contentIndex,
                    shouldRestoreFocus = false
                )
                viewModelScope.launch {
                    _effect.send(
                        MovieUiEffect.PlayAsset(mediaType = event.mediaType, assetId = event.assetId)
                    )
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