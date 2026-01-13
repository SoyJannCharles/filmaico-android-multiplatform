package com.jycra.filmaico.feature.serie.detail

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jycra.filmaico.core.ui.util.focus.BrowseFocusState
import com.jycra.filmaico.domain.serie.usecase.GetSerieByIdUseCase
import com.jycra.filmaico.domain.serie.usecase.SyncSerieContentUseCase
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
class SerieDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val syncSerieContentUseCase: SyncSerieContentUseCase,
    private val getSerieByIdUseCase: GetSerieByIdUseCase
) : ViewModel() {

    private val contentId: String = checkNotNull(savedStateHandle["serieId"])
    private val _selectedSeasonId = MutableStateFlow<String?>(null)

    private val serieFlow = getSerieByIdUseCase(contentId)

    val uiState: StateFlow<SerieDetailUiState> = combine(
        serieFlow,
        _selectedSeasonId
    ) { serie, seasonId ->

        if (serie == null) {
            return@combine SerieDetailUiState.Error("No se pudo encontrar la serie.")
        }

        val selectedSeason = if (seasonId == null) {
            serie.seasons.firstOrNull()
        } else {
            serie.seasons.find { it.id == seasonId }
        }

        if (selectedSeason == null) {
            return@combine SerieDetailUiState.Error("No se encontraron temporadas.")
        }

        SerieDetailUiState.Success(
            serie = serie,
            selectedSeason = selectedSeason,
            contentsForSeason = selectedSeason.content
        )

    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = SerieDetailUiState.Loading
    )

    private val _effect = Channel<SerieDetailUiEffect>()
    val effect = _effect.receiveAsFlow()

    var browseFocusState by mutableStateOf(BrowseFocusState())
        private set

    init {
        viewModelScope.launch {
            syncSerieContentUseCase(contentId)
        }
    }

    fun onEvent(event: SerieDetailUiEvent) {
        when (event) {
            is SerieDetailUiEvent.OnSeasonSelected -> {
                _selectedSeasonId.value = event.season.id
            }
            is SerieDetailUiEvent.OnContentClick -> {
                browseFocusState = browseFocusState.copy(
                    lastFocusedContentIndex = event.index,
                    shouldRestoreFocus = false
                )
                viewModelScope.launch {
                    _effect.send(
                        SerieDetailUiEffect.NavigateToPlayer(event.content.id)
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