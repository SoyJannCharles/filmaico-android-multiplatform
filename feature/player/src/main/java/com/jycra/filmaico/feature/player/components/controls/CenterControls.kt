package com.jycra.filmaico.feature.player.components.controls

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.jycra.filmaico.core.ui.R
import com.jycra.filmaico.domain.media.model.metadata.VideoMetadata
import com.jycra.filmaico.domain.media.util.PlayerCallbacks

@Composable
fun CenterControls(
    modifier: Modifier = Modifier,
    headerInfo: VideoMetadata,
    isPlaying: Boolean,
    callbacks: PlayerCallbacks
) {

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(24.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        if (!headerInfo.isLive) {
            IconButton(
                modifier = Modifier
                    .size(32.dp),
                onClick = callbacks.onRewindClick
            ) {
                Icon(
                    modifier = Modifier
                        .size(32.dp),
                    painter = painterResource(R.drawable.ic_player_replay_10),
                    contentDescription = "Retroceder 10 segundos"
                )
            }
        }

        IconButton(
            modifier = Modifier
                .size(32.dp),
            onClick = callbacks.onPrevClick
        ) {
            Icon(
                modifier = Modifier
                    .size(32.dp),
                painter = painterResource(R.drawable.ic_player_skip_previous),
                contentDescription = "Anterior"
            )
        }

        IconButton(
            modifier = Modifier
                .size(48.dp),
            onClick = callbacks.onPlayPauseToggle
        ) {
            Icon(
                modifier = Modifier
                    .size(48.dp),
                painter = painterResource(
                    if (isPlaying) {
                        R.drawable.ic_player_pause
                    } else {
                        R.drawable.ic_player_play
                    }
                ),
                contentDescription = if (isPlaying) "Pausar" else "Reproducir"
            )
        }

        IconButton(
            modifier = Modifier
                .size(32.dp),
            onClick = callbacks.onNextClick
        ) {
            Icon(
                modifier = Modifier
                    .size(32.dp),
                painter = painterResource(R.drawable.ic_player_skip_next),
                contentDescription = "Siguiente"
            )
        }

        if (!headerInfo.isLive) {
            IconButton(
                modifier = Modifier
                    .size(32.dp),
                onClick = callbacks.onForwardClick
            ) {
                Icon(
                    modifier = Modifier
                        .size(32.dp),
                    painter = painterResource(R.drawable.ic_player_forward_10),
                    contentDescription = "Adelantar 10 segundos"
                )
            }
        }

    }

}