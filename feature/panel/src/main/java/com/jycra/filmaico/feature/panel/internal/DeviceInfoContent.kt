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
fun DevicesInfoContent(
    modifier: Modifier = Modifier,
    uiPanel: UiPanel
) {

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {

        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {

            Text(
                text = "Sesiones Activas",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(start = 4.dp)
            )

            InfoGroup {

                uiPanel.activeSessions.forEachIndexed { index, session ->

                    InfoRow(
                        label = session.description,
                        value = session.deviceName,
                        copyable = false
                    )

                    if (index < uiPanel.activeSessions.lastIndex) {
                        HorizontalDivider(
                            color = MaterialTheme.colorScheme.surfaceBright,
                            thickness = 1.dp
                        )
                    }

                }

            }

        }

        Text(
            text = "Si no reconoces alguno de estos dispositivos, te recomendamos contactar con nosotros para cerrar sesión en todos ellos y cambiar tu contraseña.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
            modifier = Modifier.padding(horizontal = 8.dp)
        )

    }

}