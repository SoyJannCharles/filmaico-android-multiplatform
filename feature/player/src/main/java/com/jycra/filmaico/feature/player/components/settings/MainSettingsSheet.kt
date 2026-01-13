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
    onSpeedClick: () -> Unit,
    onSubtitlesClick: () -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {

        SettingsMenuItem(
            modifier = Modifier.focusRequester(focusRequester), // <--- Aquí
            icon = painterResource(R.drawable.ic_quality),
            text = "Calidad",
            onClick = onQualityClick
        )

        SettingsMenuItem(
            icon = painterResource(R.drawable.ic_speed),
            text = "Velocidad de reproducción",
            onClick = onSpeedClick
        )

        SettingsMenuItem(
            icon = painterResource(R.drawable.ic_subtitles),
            text = "Subtítulos",
            hasSubmenu = false,
            onClick = onSubtitlesClick
        )

    }

}