package com.jycra.filmaico.feature.panel

import com.jycra.filmaico.core.ui.feature.panel.model.PanelSection
import com.jycra.filmaico.core.ui.feature.panel.model.UiPanel

sealed class AccountUiState {
    object Loading : AccountUiState()
    data class Success(
        val uiPanel: UiPanel,
        val selectedSection: PanelSection = PanelSection.MY_PROFILE
    ) : AccountUiState()
    data class Error(val message: String) : AccountUiState()
    object Unauthenticated : AccountUiState()
}

sealed class PanelUiEvent {
    data class SectionSelected(val section: PanelSection) : PanelUiEvent()
    data object SignOut : PanelUiEvent()
}

sealed class PanelUiEffect {
    data object SignOut : PanelUiEffect()
}