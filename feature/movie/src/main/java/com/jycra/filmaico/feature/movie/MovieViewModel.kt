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
import com.jycra.filmaico.domain.media.usecase.GetPlayerMetadataUseCase
import com.jycra.filmaico.domain.stream.util.StreamExtractionState
import com.jycra.filmaico.shared.managers.StreamPreloadManager
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
    private val streamPreloadManager: StreamPreloadManager,
    private val getMediaContentUseCase: GetMediaContentUseCase,
    private val getPlayerMetadataUseCase: GetPlayerMetadataUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow<MovieUiState>(MovieUiState.Loading)
    val uiState = _uiState.asStateFlow()

    private val _effect = Channel<MovieUiEffect>()
    val effect = _effect.receiveAsFlow()

    var mediaFocusState by mutableStateOf(MediaFocusState())
        private set

    val extractionState: StateFlow<StreamExtractionState> = streamPreloadManager.extractionState

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

    fun preloadAsset(carouselIndex: Int, contentIndex: Int) {

        val currentState = _uiState.value

        if (currentState is MovieUiState.Success) {

            val carousel = currentState.carousels.getOrNull(carouselIndex) ?: return
            val item = carousel.items.getOrNull(contentIndex) ?: return

            viewModelScope.launch {

                val metadata = getPlayerMetadataUseCase(
                    assetId = item.id,
                    mediaType = MediaType.MOVIE
                )

                if (metadata != null && metadata.sources.isNotEmpty()) {

                    val bestSource = metadata.sources.first()

                    streamPreloadManager.startPreload(
                        assetId = metadata.assetId,
                        mediaType = metadata.mediaType,
                        source = bestSource
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