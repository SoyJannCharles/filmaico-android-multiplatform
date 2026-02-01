package com.jycra.filmaico.feature.anime.detail

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
import com.jycra.filmaico.domain.media.usecase.SyncMediaContentUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AnimeDetailViewModel @Inject constructor(
    private val syncMediaContentUseCase: SyncMediaContentUseCase,
    private val getMediaContainerUseCase: GetMediaContainerUseCase,
    savedStateHandle: SavedStateHandle
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
                syncMediaContentUseCase(containerId, MediaType.ANIME)
            } catch (e: Exception) {

            } finally {
                _isSyncing.value = false
            }
        }

    }

    val uiState: StateFlow<AnimeDetailUiState> = combine(
        getMediaContainerUseCase(containerId, MediaType.ANIME),
        _selectedSeasonId,
        _isSyncing
    ) { container, seasonId, isSyncing ->

        if (isSyncing && (container == null || container.seasons.isEmpty())) {
            return@combine AnimeDetailUiState.Loading
        }

        if (container == null) {
            return@combine AnimeDetailUiState.Error("No se pudo encontrar el Anime.")
        }

        if (!isSyncing && container.seasons.isEmpty()) {
            return@combine AnimeDetailUiState.Error("No se encontraron temporadas.")
        }

        if (seasonId == null && container.seasons.isNotEmpty()) {
            _selectedSeasonId.value = container.seasons.first().id
        }

        AnimeDetailUiState.Success(
            detail = container.toUiDetail(selectedSeasonId = seasonId ?: container.seasons.firstOrNull()?.id)
        )

    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = AnimeDetailUiState.Loading
    )

    private val _effect = Channel<AnimeDetailUiEffect>()
    val effect = _effect.receiveAsFlow()

    var mediaFocusState by mutableStateOf(MediaFocusState())
        private set

    fun onEvent(event: AnimeDetailUiEvent) {
        when (event) {
            is AnimeDetailUiEvent.OnSeasonSelected -> {
                _selectedSeasonId.value = event.seasonId
            }
            is AnimeDetailUiEvent.PlayAsset -> {
                mediaFocusState = mediaFocusState.copy(
                    lastFocusedContentIndex = event.index,
                    shouldRestoreFocus = false
                )
                viewModelScope.launch {
                    _effect.send(
                        AnimeDetailUiEffect.PlayAsset(
                            mediaType = event.mediaType,
                            assetId = event.assetId
                        )
                    )
                }
            }
            is AnimeDetailUiEvent.OnBackPressed -> {
                viewModelScope.launch {
                    _effect.send(AnimeDetailUiEffect.NavigateBack)
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