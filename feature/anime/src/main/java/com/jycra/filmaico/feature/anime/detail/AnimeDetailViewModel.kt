package com.jycra.filmaico.feature.anime.detail

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jycra.filmaico.core.ui.util.focus.BrowseFocusState
import com.jycra.filmaico.domain.anime.usecase.GetAnimeByIdUseCase
import com.jycra.filmaico.domain.anime.usecase.SyncAnimeContentUseCase
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
    savedStateHandle: SavedStateHandle,
    private val syncAnimeContentUseCase: SyncAnimeContentUseCase,
    private val getAnimeByIdUseCase: GetAnimeByIdUseCase
) : ViewModel() {

    private val contentId: String = checkNotNull(savedStateHandle["animeId"])
    private val _selectedSeasonId = MutableStateFlow<String?>(null)

    private val animeFlow = getAnimeByIdUseCase(contentId)

    val uiState: StateFlow<AnimeDetailUiState> = combine(
        animeFlow,
        _selectedSeasonId
    ) { anime, seasonId ->

        if (anime == null) {
            return@combine AnimeDetailUiState.Error("No se pudo encontrar la serie.")
        }

        val selectedSeason = if (seasonId == null) {
            anime.seasons.firstOrNull()
        } else {
            anime.seasons.find { it.id == seasonId }
        }

        if (selectedSeason == null) {
            return@combine AnimeDetailUiState.Error("No se encontraron temporadas.")
        }

        AnimeDetailUiState.Success(
            anime = anime,
            selectedSeason = selectedSeason,
            contentsForSeason = selectedSeason.content
        )

    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = AnimeDetailUiState.Loading
    )

    private val _effect = Channel<AnimeDetailUiEffect>()
    val effect = _effect.receiveAsFlow()

    var browseFocusState by mutableStateOf(BrowseFocusState())
        private set

    init {
        viewModelScope.launch {
            syncAnimeContentUseCase(contentId)
        }
    }

    fun onEvent(event: AnimeDetailUiEvent) {
        when (event) {
            is AnimeDetailUiEvent.OnSeasonSelected ->
                _selectedSeasonId.value = event.season.id
            is AnimeDetailUiEvent.OnContentClick -> {
                viewModelScope.launch {
                    browseFocusState = browseFocusState.copy(
                        lastFocusedContentIndex = event.index,
                        shouldRestoreFocus = false
                    )
                    viewModelScope.launch {
                        _effect.send(
                            AnimeDetailUiEffect.NavigateToPlayer(event.content.id)
                        )
                    }
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
        browseFocusState = browseFocusState.copy(hasConsumedInitialFocus = true)
    }

    fun markFocusRestored() {
        browseFocusState = browseFocusState.copy(shouldRestoreFocus = false)
    }

}