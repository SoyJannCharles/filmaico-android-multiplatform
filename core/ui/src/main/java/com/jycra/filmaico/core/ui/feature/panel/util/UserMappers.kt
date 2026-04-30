package com.jycra.filmaico.core.ui.feature.panel.util

import com.jycra.filmaico.core.ui.feature.panel.model.PanelOption
import com.jycra.filmaico.core.ui.feature.panel.model.PanelSection
import com.jycra.filmaico.core.ui.feature.panel.model.UiPanel
import com.jycra.filmaico.core.ui.feature.panel.model.UiSession
import com.jycra.filmaico.domain.user.model.User
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun User.toUiPanel(appVersion: String): UiPanel {

    val dateFormatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    return UiPanel(
        appVersion = "Versión: $appVersion",
        email = this.email ?: "Sin correo registrado",
        subscriptionStatus = if (this.subscription?.isActive() ?: false) "Activa" else "Expirada",
        expirationDate = "Vence: ${dateFormatter.format(this.subscription?.expirationDate ?: Date())}",
        maxDevices = "${this.subscription?.maxDevices} dispositivos permitidos",
        activeSessions = this.activeSessions.map { session ->
            UiSession(
                id = session.sessionId,
                deviceName = session.deviceInfo.split(";").firstOrNull()
                    ?: "Dispositivo desconocido",
                description = "Sesión iniciada el ${dateFormatter.format(session.loginDate)}",
                isCurrent = false
            )
        },
        panelOptions = listOf(
            PanelOption(PanelSection.MY_PROFILE, "Mi Perfil", "Datos personales y Suscripción"),
            PanelOption(PanelSection.MY_DEVICES, "Mis Dispositivos", "Gestiona tus equipos conectados"),
            PanelOption(PanelSection.LOGOUT, "Cerrar Sesión", "Te estaremos esperando, nos vemos!")
        )
    )

}