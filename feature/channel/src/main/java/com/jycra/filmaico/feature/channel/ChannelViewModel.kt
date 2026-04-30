package com.jycra.filmaico.feature.channel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jycra.filmaico.core.ui.feature.media.util.mapper.toUiCarousels
import com.jycra.filmaico.core.ui.util.focus.MediaFocusState
import com.jycra.filmaico.domain.media.model.Media
import com.jycra.filmaico.domain.media.model.MediaType
import com.jycra.filmaico.domain.media.usecase.GetCurrentEpgUseCase
import com.jycra.filmaico.domain.media.usecase.GetMediaContentUseCase
import com.jycra.filmaico.domain.media.usecase.GetPlayerMetadataUseCase
import com.jycra.filmaico.shared.managers.StreamPreloadManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChannelViewModel @Inject constructor(
    private val streamPreloadManager: StreamPreloadManager,
    private val getMediaContentUseCase: GetMediaContentUseCase,
    private val getPlayerMetadataUseCase: GetPlayerMetadataUseCase,
    private val getCurrentEpgUseCase: GetCurrentEpgUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<ChannelUiState>(ChannelUiState.Loading)
    val uiState = _uiState.asStateFlow()

    private val _effect = Channel<ChannelUiEffect>()
    val effect = _effect.receiveAsFlow()

    var mediaFocusState by mutableStateOf(MediaFocusState())
        private set

    init {
        observeContent()
    }

    private fun observeContent() {
        viewModelScope.launch {
            combine(
                getMediaContentUseCase(mediaType = MediaType.CHANNEL),
                getCurrentEpgUseCase()
            ) { carousels, epgMap ->

                val uiCarousels = carousels.map { it.toUiCarousels() }

                uiCarousels.map { carousel ->

                    carousel.copy(
                        items = carousel.items.map { uiItem ->

                            val program = epgMap[uiItem.epgId]

                            if (program != null) {
                                uiItem.copy(
                                    name = uiItem.name,
                                    epgTitle = program.title,
                                    epgDescription = program.description,
                                    epgStartTime = program.startTime,
                                    epgEndTime = program.endTime
                                )
                            } else {
                                uiItem
                            }

                        }
                    )

                }

            }
                .onStart { _uiState.value = ChannelUiState.Loading }
                .catch { e -> _uiState.value = ChannelUiState.Error(e.message ?: "Ocurrió un error") }
                .collect { updatedCarousels ->
                    _uiState.value = ChannelUiState.Success(carousels = updatedCarousels)
                }
        }
    }

    fun onEvent(event: ChannelUiEvent) {
        when (event) {
            is ChannelUiEvent.PlayAsset -> {
                mediaFocusState = mediaFocusState.copy(
                    lastFocusedCarouselIndex = event.carouselIndex,
                    lastFocusedContentIndex = event.contentIndex,
                    shouldRestoreFocus = false
                )
                viewModelScope.launch {
                    _effect.send(
                        ChannelUiEffect.PlayAsset(assetId = event.assetId)
                    )
                }
            }
        }
    }

    fun preloadAsset(carouselIndex: Int, contentIndex: Int) {

        val currentState = _uiState.value

        if (currentState is ChannelUiState.Success) {

            val carousel = currentState.carousels.getOrNull(carouselIndex) ?: return
            val item = carousel.items.getOrNull(contentIndex) ?: return

            viewModelScope.launch {

                val metadata = getPlayerMetadataUseCase(
                    assetId = item.id,
                    mediaType = MediaType.CHANNEL
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