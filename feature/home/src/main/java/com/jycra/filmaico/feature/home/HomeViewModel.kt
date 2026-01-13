package com.jycra.filmaico.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jycra.filmaico.core.navigation.ContentType
import com.jycra.filmaico.domain.user.usecase.GetCurrentUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.util.Date
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getCurrentUserUseCase: GetCurrentUserUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val uiState = _uiState.asStateFlow()

    private val _effect = Channel<HomeUiEffect>()
    val effect = _effect.receiveAsFlow()

    init {
        loadUserData()
    }

    private fun loadUserData() {
        viewModelScope.launch {

            _uiState.value = HomeUiState.Loading
            val user = getCurrentUserUseCase()

            if (user != null) {

                val expirationDate = user.subscription.expirationDate
                val currentDate = Date()

                val diffInMillis = expirationDate.time - currentDate.time

                val daysRemaining = TimeUnit.MILLISECONDS.toDays(diffInMillis).toInt()

                _uiState.value = HomeUiState.Success(
                    subscriptionDaysRemaining = daysRemaining.coerceAtLeast(0) // Muestra 0 si la fecha ya pasó
                )

            } else {
                _uiState.value = HomeUiState.Error("Usuario no encontrado.")
            }

        }
    }

    fun onEvent(event: HomeUiEvent) {
        viewModelScope.launch {
            val effectToSend = when (event) {
                is HomeUiEvent.OnAnimeClick -> HomeUiEffect.NavigateToDetail(ContentType.ANIME, event.animeId)
                is HomeUiEvent.OnSerieClick -> HomeUiEffect.NavigateToDetail(ContentType.SERIE, event.serieId)
                is HomeUiEvent.OnMovieClick -> HomeUiEffect.NavigateToDetail(ContentType.MOVIE, event.movieId)
                is HomeUiEvent.OnChannelClick -> HomeUiEffect.NavigateToPlayer(ContentType.CHANNEL, event.channelId)
                is HomeUiEvent.OnProfileClick -> HomeUiEffect.NavigateToProfile
            }
            _effect.send(effectToSend)
        }
    }

}