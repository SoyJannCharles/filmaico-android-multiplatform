package com.jycra.filmaico.feature.movie.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jycra.filmaico.domain.movie.usecase.GetMovieByIdUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getMovieByIdUseCase: GetMovieByIdUseCase
) : ViewModel() {

    private val movieId: String = checkNotNull(savedStateHandle["movieId"])

    private val _uiState = MutableStateFlow<MovieDetailUiState>(MovieDetailUiState.Loading)
    val uiState = _uiState.asStateFlow()

    private val _effect = Channel<MovieDetailUiEffect>()
    val effect = _effect.receiveAsFlow()

    init {
        loadMovieDetails()
    }

    private fun loadMovieDetails() {
        _uiState.value = MovieDetailUiState.Loading
        viewModelScope.launch {
            val movie = getMovieByIdUseCase(movieId)
            if (movie != null) {
                _uiState.value = MovieDetailUiState.Success(movie = movie)
            } else {
                _uiState.value = MovieDetailUiState.Error("No se pudo encontrar la pelicula.")
            }
        }
    }

    fun onEvent(event: MovieDetailUiEvent) {
        when (event) {
            is MovieDetailUiEvent.OnStartPlayback -> navigateToPlayer(event.movie.id)
            is MovieDetailUiEvent.OnBackPressed -> navigateBack()
        }
    }

    private fun navigateToPlayer(movieId: String) {
        viewModelScope.launch {
            _effect.send(MovieDetailUiEffect.NavigateToPlayer(movieId))
        }
    }

    private fun navigateBack() {
        viewModelScope.launch {
            _effect.send(MovieDetailUiEffect.NavigateBack)
        }
    }

}