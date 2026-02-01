package com.jycra.filmaico.feature.anime

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
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AnimeViewModel @Inject constructor(
    private val getMediaContentUseCase: GetMediaContentUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<AnimeUiState>(AnimeUiState.Loading)
    val uiState: StateFlow<AnimeUiState> = _uiState.asStateFlow()

    private val _effect = Channel<AnimeUiEffect>()
    val effect = _effect.receiveAsFlow()

    var mediaFocusState by mutableStateOf(MediaFocusState())
        private set

    init {
        observeContent()
    }

    private fun observeContent() {
        viewModelScope.launch {
            getMediaContentUseCase(mediaType = MediaType.ANIME)
                .onStart { _uiState.value = AnimeUiState.Loading }
                .catch { e -> _uiState.value = AnimeUiState.Error(e.message ?: "Ocurrió un error") }
                .collect { carousels ->
                    _uiState.value = AnimeUiState.Success(
                        carousels = carousels.map { carousel ->
                            carousel.toUiCarousels()
                        }
                    )
                }
        }
    }

    fun onEvent(event: AnimeUiEvent) {
        when (event) {
            is AnimeUiEvent.OpenDetail -> {
                mediaFocusState = mediaFocusState.copy(
                    lastFocusedCarouselId = event.carouselId,
                    lastFocusedCarouselIndex = event.carouselIndex,
                    lastFocusedContentIndex = event.contentIndex,
                    shouldRestoreFocus = false
                )
                viewModelScope.launch {
                    _effect.send(AnimeUiEffect.OpenDetail(containerId = event.containerId))
                }
            }
            is AnimeUiEvent.PlayAsset -> {
                mediaFocusState = mediaFocusState.copy(
                    lastFocusedCarouselIndex = event.carouselIndex,
                    lastFocusedContentIndex = event.contentIndex,
                    shouldRestoreFocus = false
                )
                viewModelScope.launch {
                    _effect.send(AnimeUiEffect.PlayAsset(
                        mediaType = event.mediaType,
                        assetId = event.assetId
                    ))
                }
            }
        }
    }

    fun onScreenResumed() {

        val currentState = _uiState.value
        if (currentState is AnimeUiState.Success) {

            val targetCarouselIndex = currentState.carousels.indexOfFirst {
                it.id == mediaFocusState.lastFocusedCarouselId
            }

            val carouselIndex =
                if (targetCarouselIndex != -1) targetCarouselIndex
                else mediaFocusState.lastFocusedCarouselIndex
            val targetCarousel = currentState.carousels.getOrNull(carouselIndex)

            val contentIndex = if (targetCarousel != null) {
                mediaFocusState.lastFocusedContentIndex
                    .coerceAtMost(targetCarousel.items.lastIndex.coerceAtLeast(0))
            } else 0

            mediaFocusState = mediaFocusState.copy(
                lastFocusedCarouselIndex = carouselIndex,
                lastFocusedContentIndex = contentIndex,
                shouldRestoreFocus = true
            )

        } else {

            mediaFocusState = mediaFocusState.copy(shouldRestoreFocus = true)

        }

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