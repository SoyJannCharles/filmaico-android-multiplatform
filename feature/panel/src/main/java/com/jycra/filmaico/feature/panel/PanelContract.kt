package com.jycra.filmaico.feature.panel

import com.jycra.filmaico.core.ui.feature.panel.model.PanelSection
import com.jycra.filmaico.core.ui.feature.panel.model.UiPanel
import com.jycra.filmaico.domain.user.error.AuthError

sealed class AccountUiState {
    object Loading : AccountUiState()
    data class Success(
        val uiPanel: UiPanel,
        val selectedSection: PanelSection = PanelSection.MY_PROFILE,
        val linkingCode: String = "",
        val linkingError: AuthError? = null,
        val isLinking: Boolean = false
    ) : AccountUiState()
    data class Error(val message: String) : AccountUiState()
    object Unauthenticated : AccountUiState()
}

sealed class PanelUiEvent {
    data class SectionSelected(val section: PanelSection) : PanelUiEvent()
    data class OnLinkingCodeChange(val code: String) : PanelUiEvent()
    data object LinkDeviceTriggered : PanelUiEvent()
    data object SignOut : PanelUiEvent()
}

sealed class PanelUiEffect {
    data object SignOut : PanelUiEffect()
}