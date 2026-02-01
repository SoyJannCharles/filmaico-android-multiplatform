package com.jycra.filmaico.feature.panel.internal

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jycra.filmaico.core.ui.component.common.InfoGroup
import com.jycra.filmaico.core.ui.component.common.InfoRow
import com.jycra.filmaico.core.ui.feature.panel.model.UiPanel

@Composable
fun ProfileInfoContent(
    modifier: Modifier = Modifier,
    uiPanel: UiPanel
) {

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {

        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {

            Text(
                text = "Datos de Cuenta",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(start = 4.dp)
            )

            InfoGroup {
                InfoRow(label = "Correo Electrónico", value = uiPanel.email)
                HorizontalDivider(color = MaterialTheme.colorScheme.surfaceBright, thickness = 1.dp)
                InfoRow(label = "Contraseña", value = "••••••••••••")
            }

        }

        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {

            Text(
                text = "Suscripción",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(start = 4.dp)
            )

            InfoGroup {
                InfoRow(label = "Estado", value = uiPanel.subscriptionStatus)
                HorizontalDivider(color = MaterialTheme.colorScheme.surfaceBright, thickness = 1.dp)
                InfoRow(label = "Vencimiento", value = uiPanel.expirationDate)
                HorizontalDivider(color = MaterialTheme.colorScheme.surfaceBright, thickness = 1.dp)
                InfoRow(label = "Dispositivos permitidos", value = uiPanel.maxDevices)
            }

        }

    }

}