package com.jycra.filmaico.feature.serie.detail

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jycra.filmaico.core.ui.feature.media.util.mapper.toUiDetail
import com.jycra.filmaico.core.ui.util.focus.MediaFocusState
import com.jycra.filmaico.domain.media.model.MediaType
import com.jycra.filmaico.domain.media.usecase.GetMediaContainerUseCase
import com.jycra.filmaico.domain.media.usecase.GetPlayerMetadataUseCase
import com.jycra.filmaico.domain.media.usecase.SyncMediaContentUseCase
import com.jycra.filmaico.shared.managers.StreamPreloadManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SerieDetailViewModel @Inject constructor(
    private val streamPreloadManager: StreamPreloadManager,
    private val syncMediaContentUseCase: SyncMediaContentUseCase,
    private val getMediaContainerUseCase: GetMediaContainerUseCase,
    private val getPlayerMetadataUseCase: GetPlayerMetadataUseCase,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val containerId: String = checkNotNull(savedStateHandle["containerId"])
    private val _selectedSeasonId = MutableStateFlow<String?>(null)

    private val _isSyncing = MutableStateFlow(true)

    init {
        syncContent()
    }

    private fun syncContent() {

        viewModelScope.launch {
            _isSyncing.value = true
            try {
                syncMediaContentUseCase(containerId, MediaType.SERIE)
            } catch (e: Exception) {

            } finally {
                _isSyncing.value = false
            }
        }

    }

    val uiState: StateFlow<SerieDetailUiState> = combine(
        getMediaContainerUseCase(containerId, MediaType.SERIE),
        _selectedSeasonId,
        _isSyncing
    ) { container, seasonId, isSyncing ->

        if (isSyncing && (container == null || container.seasons.isEmpty())) {
            return@combine SerieDetailUiState.Loading
        }

        if (container == null) {
            return@combine SerieDetailUiState.Error("No se pudo encontrar el Anime.")
        }

        if (!isSyncing && container.seasons.isEmpty()) {
            return@combine SerieDetailUiState.Error("No se encontraron temporadas.")
        }

        if (seasonId == null && container.seasons.isNotEmpty()) {
            _selectedSeasonId.value = container.seasons.first().id
        }

        SerieDetailUiState.Success(
            detail = container.toUiDetail(selectedSeasonId = seasonId ?: container.seasons.firstOrNull()?.id)
        )

    }
        .onEach { state ->
            if (state is SerieDetailUiState.Success) {

                if (!mediaFocusState.shouldRestoreFocus && mediaFocusState.lastFocusedContentIndex == 0) {

                    val firstEpisode = state.detail.selectedSeasonContents.firstOrNull()

                    if (firstEpisode != null) {

                        val metadata = getPlayerMetadataUseCase(
                            assetId = firstEpisode.id,
                            mediaType = firstEpisode.mediaType
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
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = SerieDetailUiState.Loading
        )

    private val _effect = Channel<SerieDetailUiEffect>()
    val effect = _effect.receiveAsFlow()

    var mediaFocusState by mutableStateOf(MediaFocusState())
        private set

    fun onEvent(event: SerieDetailUiEvent) {
        when (event) {
            is SerieDetailUiEvent.OnSeasonSelected -> {
                _selectedSeasonId.value = event.seasonId
            }
            is SerieDetailUiEvent.PlayAsset -> {
                mediaFocusState = mediaFocusState.copy(
                    lastFocusedContentIndex = event.index,
                    shouldRestoreFocus = false
                )
                viewModelScope.launch {
                    _effect.send(
                        SerieDetailUiEffect.PlayAsset(
                            mediaType = event.mediaType,
                            assetId = event.assetId
                        )
                    )
                }
            }
            is SerieDetailUiEvent.OnBackPressed -> {
                viewModelScope.launch {
                    _effect.send(SerieDetailUiEffect.NavigateBack)
                }
            }
        }
    }

    fun preloadAsset(contentIndex: Int) {

        val currentState = uiState.value

        if (currentState is SerieDetailUiState.Success) {

            val episode = currentState.detail.selectedSeasonContents.getOrNull(contentIndex) ?: return

            viewModelScope.launch {

                val metadata = getPlayerMetadataUseCase(
                    assetId = episode.id,
                    mediaType = episode.mediaType
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