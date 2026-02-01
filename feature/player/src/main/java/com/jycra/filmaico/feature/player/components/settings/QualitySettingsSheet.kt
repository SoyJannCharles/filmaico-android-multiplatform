package com.jycra.filmaico.feature.player.components.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.jycra.filmaico.core.player.model.VideoQuality
import com.jycra.filmaico.core.ui.R

@Composable
fun QualitySettingsSheet(
    focusRequester: FocusRequester,
    qualities: List<VideoQuality>,
    currentQuality: VideoQuality?,
    onQualitySelected: (VideoQuality) -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {

        QualitySheetHeader(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 16.dp),
            currentQuality = currentQuality
        )

        LazyColumn {
            itemsIndexed(qualities) { index, quality ->
                QualityItem(
                    quality = quality,
                    isSelected = quality == currentQuality,
                    modifier = if (index == 0) Modifier.focusRequester(focusRequester) else Modifier,
                    onClick = { onQualitySelected(quality) }
                )
            }
        }

    }

}

@Composable
private fun QualitySheetHeader(
    currentQuality: VideoQuality?,
    modifier: Modifier = Modifier
) {

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {

        val displayText = if (currentQuality?.isAuto == true) "Auto" else "${currentQuality?.height ?: "Auto"}p"

        Text(
            text = "Calidad de Video · $displayText",
            style = MaterialTheme.typography.bodyLarge,
            color = Color.White
        )
    }

}

@Composable
private fun QualityItem(
    quality: VideoQuality,
    isSelected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {

    var isFocused by remember { mutableStateOf(false) }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = if (isFocused) MaterialTheme.colorScheme.surfaceContainerLow.copy(0.64f) else Color.Transparent,
                shape = RoundedCornerShape(8.dp)
            )
            .onFocusChanged { isFocused = it.isFocused }
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            )
            .padding(horizontal = 16.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(end = 16.dp)
        ) {

            Text(
                text = quality.label,
                style = MaterialTheme.typography.bodyLarge,
                color = Color.White
            )

            Text(
                text = if (quality.isAuto) {
                    "Selecciona la mejor calidad sin afectar el rendimiento de tu red"
                } else {
                    quality.bitrateLabel
                },
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White.copy(0.8f)
            )

        }

        if (isSelected) {
            Icon(
                painter = painterResource(R.drawable.ic_check),
                contentDescription = "Seleccionado",
                tint = Color.White
            )
        }

    }

}