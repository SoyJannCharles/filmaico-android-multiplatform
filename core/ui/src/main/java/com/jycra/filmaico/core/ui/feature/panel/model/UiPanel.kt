package com.jycra.filmaico.core.ui.feature.panel.model

data class UiPanel(
    val email: String,
    val subscriptionStatus: String,
    val expirationDate: String,
    val maxDevices: String,
    val activeSessions: List<UiSession>,
    val panelOptions: List<PanelOption>
)