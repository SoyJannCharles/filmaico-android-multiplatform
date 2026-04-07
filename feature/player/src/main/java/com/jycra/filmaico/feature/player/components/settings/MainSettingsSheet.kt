package com.jycra.filmaico.feature.player.components.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.jycra.filmaico.core.ui.R

@Composable
fun MainSettingsSheet(
    focusRequester: FocusRequester,
    onQualityClick: () -> Unit,
    onProviderClick: () -> Unit,
    onAudioClick: () -> Unit,
    onDismiss: () -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {

        SettingsMenuItem(
            modifier = Modifier.focusRequester(focusRequester),
            icon = painterResource(R.drawable.ic_quality),
            text = "Calidad",
            onDismiss = onDismiss,
            onClick = onQualityClick
        )

        SettingsMenuItem(
            icon = painterResource(R.drawable.ic_host_server),
            text = "Proveedor",
            onDismiss = onDismiss,
            onClick = onProviderClick
        )

        SettingsMenuItem(
            icon = painterResource(R.drawable.ic_anime),
            text = "Audio",
            onDismiss = onDismiss,
            onClick = onAudioClick
        )

    }

}