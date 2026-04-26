package com.jycra.filmaico.feature.panel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jycra.filmaico.core.app.SessionManager
import com.jycra.filmaico.core.ui.feature.panel.util.toUiPanel
import com.jycra.filmaico.core.ui.util.focus.MediaFocusState
import com.jycra.filmaico.domain.user.usecase.LinkDeviceWithCodeUseCase
import com.jycra.filmaico.domain.user.usecase.SignOutUseCase
import com.jycra.filmaico.domain.user.util.AuthResult
import com.jycra.filmaico.domain.user.util.SessionStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PanelViewModel @Inject constructor(
    private val sessionManager: SessionManager,
    private val linkDeviceWithCodeUseCase: LinkDeviceWithCodeUseCase,
    private val signOutUseCase: SignOutUseCase
) : ViewModel() {

    private val _localState = MutableStateFlow(PanelLocalState())

    val uiState: StateFlow<AccountUiState> = combine(
        sessionManager.sessionStatus,
        _localState
    ) { status, local ->
        when (status) {
            is SessionStatus.Authenticated -> {
                AccountUiState.Success(
                    uiPanel = status.user.toUiPanel(),
                    selectedSection = local.selectedSection,
                    linkingCode = local.linkingCode,
                    isLinking = local.isLinking,
                    linkingError = local.linkingError
                )
            }
            is SessionStatus.Unauthenticated -> AccountUiState.Unauthenticated
            else -> AccountUiState.Loading
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), AccountUiState.Loading)

    private val _effect = Channel<PanelUiEffect>()
    val effect = _effect.receiveAsFlow()

    var mediaFocusState by mutableStateOf(MediaFocusState())
        private set

    fun onEvent(event: PanelUiEvent) {
        when (event) {
            is PanelUiEvent.SectionSelected -> {
                _localState.update { it.copy(selectedSection = event.section) }
            }
            is PanelUiEvent.OnLinkingCodeChange -> {
                _localState.update { it.copy(linkingCode = event.code) }
            }
            PanelUiEvent.LinkDeviceTriggered -> {
                val code = _localState.value.linkingCode
                if (code.length == 6) {
                    viewModelScope.launch {
                        _localState.update { it.copy(isLinking = true, linkingError = null) }

                        when (val result = linkDeviceWithCodeUseCase(code)) {
                            is AuthResult.Success -> {
                                _localState.update { it.copy(isLinking = false, linkingCode = "") }
                            }
                            is AuthResult.Failure -> {
                                _localState.update { it.copy(isLinking = false, linkingError = result.failure) }
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