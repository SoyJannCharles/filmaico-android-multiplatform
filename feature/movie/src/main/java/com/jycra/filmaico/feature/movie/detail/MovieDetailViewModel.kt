package com.jycra.filmaico.feature.movie.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jycra.filmaico.core.player.PlayerManager
import com.jycra.filmaico.domain.stream.util.MediaType
import com.jycra.filmaico.domain.media.usecase.GetMediaContainerUseCase
import com.jycra.filmaico.domain.media.usecase.GetStreamMetadataUseCase
import com.jycra.filmaico.domain.stream.util.StreamExtractionState
import com.jycra.filmaico.shared.managers.StreamManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieDetailViewModel @Inject constructor(
    private val streamManager: StreamManager,
    private val playerManager: PlayerManager,
    private val getMediaContainerUseCase: GetMediaContainerUseCase,
    private val getStreamMetadataUseCase: GetStreamMetadataUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val containerId: String = checkNotNull(savedStateHandle["containerId"])

    private val _uiState = MutableStateFlow<MovieDetailUiState>(MovieDetailUiState.Loading)
    val uiState = _uiState.asStateFlow()

    private val _effect = Channel<MovieDetailUiEffect>()
    val effect = _effect.receiveAsFlow()

    val extractionState: StateFlow<StreamExtractionState> = streamManager.extractionState

    init {
        loadDetails()
        preloadStreamSilently()
    }

    private fun loadDetails() {
        viewModelScope.launch {
            getMediaContainerUseCase(containerId, MediaType.MOVIE).collect { container ->
                if (container != null) {
                    _uiState.value = MovieDetailUiState.Success(media = container)
                } else {
                    _uiState.value = MovieDetailUiState.Error("No se pudo encontrar la película.")
                }
            }
        }
    }

    private fun preloadStreamSilently() {
        viewModelScope.launch(Dispatchers.IO) {

            try {

                val metadata = getStreamMetadataUseCase(
                    assetId = containerId,
                    mediaType = MediaType.MOVIE
                )

                if (metadata != null && metadata.sources.isNotEmpty()) {

                    val bestSource = metadata.sources.first()

                    /*val urlResolved = streamManager.getStream(
                        assetId = metadata.assetId,
                        mediaType = metadata.mediaType,
                        source = bestSource
                    )

                    urlResolved.fold(
                        onSuccess = { playbackData ->
                            playerManager.prepareInBackground(playbackData)
                        },
                        onFailure = { error ->
                            // manejar error
                        }
                    )*/

                }

            } catch (e: Exception) {

            }

        }
    }

    fun onEvent(event: MovieDetailUiEvent) {
        when (event) {
            is MovieDetailUiEvent.PlayAsset -> {
                viewModelScope.launch {
                    _effect.send(MovieDetailUiEffect.NavigateToPlayer(containerId))
                }
            }
            is MovieDetailUiEvent.OnBackPressed -> {
                viewModelScope.launch {
                    _effect.send(MovieDetailUiEffect.NavigateBack)
                }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        streamManager.clear()
    }

}