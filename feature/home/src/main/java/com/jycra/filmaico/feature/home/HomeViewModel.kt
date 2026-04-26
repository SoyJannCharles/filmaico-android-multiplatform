package com.jycra.filmaico.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jycra.filmaico.core.app.AppViewModel
import com.jycra.filmaico.core.app.SessionManager
import com.jycra.filmaico.core.ui.util.formatTimeRemaining
import com.jycra.filmaico.domain.user.model.User
import com.jycra.filmaico.domain.user.util.SessionStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Date
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val sessionManager: SessionManager
) : ViewModel() {

    val uiState: StateFlow<HomeUiState> = sessionManager.sessionStatus
        .map { session ->
            if (session is SessionStatus.Authenticated) {

                val user = session.user

                val expiry = user.subscription?.expirationDate?.time ?: System.currentTimeMillis()
                val diff = expiry - System.currentTimeMillis()

                HomeUiState.Ready(
                    expirationTimestamp = expiry,
                    expirationText = formatTimeRemaining(diff),
                    isSubscriptionUrgent = diff < TimeUnit.DAYS.toMillis(5)
                )

            } else {
                HomeUiState.Idle
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = HomeUiState.Loading
        )

    private val _effect = Channel<HomeUiEffect>()
    val effect = _effect.receiveAsFlow()

    fun onEvent(event: HomeUiEvent) {
        viewModelScope.launch {
            when (event) {
                is HomeUiEvent.OpenDetail -> {
                    _effect.send(HomeUiEffect.OpenDetail(
                        mediaType = event.mediaType,
                        containerId = event.containerId
                    ))
                }
                is HomeUiEvent.PlayAsset -> {
                    _effect.send(HomeUiEffect.PlayAsset(
                        mediaType = event.mediaType,
                        assetId = event.assetId
                    ))
                }
                is HomeUiEvent.OnProfileClick -> HomeUiEffect.NavigateToProfile
            }
        }
    }

}