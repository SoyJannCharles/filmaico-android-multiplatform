package com.jycra.filmaico.feature.panel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jycra.filmaico.core.ui.feature.panel.util.toUiPanel
import com.jycra.filmaico.core.ui.util.focus.MediaFocusState
import com.jycra.filmaico.domain.user.usecase.GetCurrentUserUseCase
import com.jycra.filmaico.domain.user.usecase.LinkDeviceWithCodeUseCase
import com.jycra.filmaico.domain.user.usecase.SignOutUseCase
import com.jycra.filmaico.domain.user.util.AuthResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PanelViewModel @Inject constructor(
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val linkDeviceWithCodeUseCase: LinkDeviceWithCodeUseCase,
    private val signOutUseCase: SignOutUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<AccountUiState>(AccountUiState.Loading)
    val uiState = _uiState.asStateFlow()

    private val _effect = Channel<PanelUiEffect>()
    val effect = _effect.receiveAsFlow()

    var mediaFocusState by mutableStateOf(MediaFocusState())
        private set

    init {
        getUser()
    }

    private fun getUser() {

        viewModelScope.launch {
            _uiState.value = AccountUiState.Loading
            try {
                val user = getCurrentUserUseCase()
                if (user != null) {
                    _uiState.value = AccountUiState.Success(uiPanel = user.toUiPanel())
                } else {
                    _uiState.value = AccountUiState.Unauthenticated
                }
            } catch (e: Exception) {
                _uiState.value = AccountUiState.Error("No se pudo cargar la información")
            }
        }

    }

    fun onEvent(event: PanelUiEvent) {
        when (event) {
            is PanelUiEvent.SectionSelected -> {
                _uiState.update { currentState ->
                    if (currentState is AccountUiState.Success) {
                        currentState.copy(selectedSection = event.section)
                    } else currentState
                }
            }

            is PanelUiEvent.OnLinkingCodeChange -> {
                _uiState.update { currentState ->
                    if (currentState is AccountUiState.Success) {
                        currentState.copy(linkingCode = event.code)
                    } else currentState
                }
            }

            PanelUiEvent.LinkDeviceTriggered -> {
                val currentState = _uiState.value
                if (currentState is AccountUiState.Success && currentState.linkingCode.length == 6) {
                    viewModelScope.launch {
                        _uiState.update {
                            (it as AccountUiState.Success).copy(
                                isLinking = true,
                                linkingError = null
                            )
                        }
                        when (val result = linkDeviceWithCodeUseCase(currentState.linkingCode)) {
                            is AuthResult.Success -> {
                                _uiState.update {
                                    (it as AccountUiState.Success).copy(
                                        isLinking = false,
                                        linkingCode = ""
                                    )
                                }
                            }

                            is AuthResult.Failure -> {
                                _uiState.update {
                                    (it as AccountUiState.Success).copy(
                                        isLinking = false,
                                        linkingError = result.failure
                                    )
                                }
                            }
                        }
                    }
                }
            }

            is PanelUiEvent.SignOut -> {
                viewModelScope.launch {
                    signOutUseCase()
                    _effect.send(PanelUiEffect.SignOut)
                }
            }
        }
    }

    fun onScreenResumed() {
        mediaFocusState = mediaFocusState.copy(
            shouldRestoreFocus = true
        )
    }

    fun saveFocusPosition(carouselIndex: Int) {
        mediaFocusState = mediaFocusState.copy(
            lastFocusedCarouselIndex = carouselIndex
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