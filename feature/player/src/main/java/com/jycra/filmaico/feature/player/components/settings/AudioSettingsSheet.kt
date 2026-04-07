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
import com.jycra.filmaico.domain.stream.model.metadata.AudioMetadata
import com.jycra.filmaico.core.ui.R

@Composable
fun AudioSettingsSheet(
    focusRequester: FocusRequester,
    audioMetadata: List<AudioMetadata>,
    currentAudioMetadata: AudioMetadata?,
    onAudioSelected: (AudioMetadata) -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {

        AudioSheetHeader(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 16.dp),
            currentAudioMetadata = currentAudioMetadata
        )

        LazyColumn {
            itemsIndexed(audioMetadata) { index, audio ->
                AudioItem(
                    audioMetadata = audio,
                    isSelected = audio == currentAudioMetadata,
                    modifier = if (index == 0) Modifier.focusRequester(focusRequester) else Modifier,
                    onClick = { onAudioSelected(audio) }
                )
            }
        }

    }

}

@Composable
private fun AudioSheetHeader(
    modifier: Modifier = Modifier,
    currentAudioMetadata: AudioMetadata?
) {

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {

        val displayText = currentAudioMetadata?.displayAudio ?: "Desconocido"

        Text(
            text = "Audio · $displayText",
            style = MaterialTheme.typography.bodyLarge,
            color = Color.White
        )

    }

}

@Composable
private fun AudioItem(
    modifier: Modifier = Modifier,
    audioMetadata: AudioMetadata,
    isSelected: Boolean,
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
                text = audioMetadata.displayAudio,
                style = MaterialTheme.typography.bodyLarge,
                color = Color.White
            )

            audioMetadata.displaySubtitle?.let { subtitle ->
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White.copy(0.8f)
                )
            }

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