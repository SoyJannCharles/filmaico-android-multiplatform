package com.jycra.filmaico.feature.player.components.controls

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.jycra.filmaico.core.navigation.Platform
import com.jycra.filmaico.core.ui.R
import com.jycra.filmaico.feature.player.PlaybackState
import com.jycra.filmaico.feature.player.model.VideoMetadata
import com.jycra.filmaico.feature.player.util.PlayerCallbacks

@Composable
fun BottomControls(
    modifier: Modifier = Modifier,
    platform: Platform,
    headerInfo: VideoMetadata,
    playbackState: PlaybackState,
    callbacks: PlayerCallbacks,
    sliderFocusRequester: FocusRequester,
    playButtonFocusRequester: FocusRequester
) {

    Column(
        modifier = modifier
            .fillMaxWidth()
    ) {

        if (!headerInfo.isLive) {
            PlayerSlider(
                platform = platform,
                currentPosition = playbackState.currentPosition,
                totalDuration = playbackState.totalDuration,
                onPlayPauseToggle = callbacks.onPlayPauseToggle,
                onSeekTo = callbacks.onSeekTo,
                focusRequester = sliderFocusRequester,
                playButtonFocusRequester = playButtonFocusRequester
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {

            if (platform == Platform.TV) {

                IconButton(
                    onClick = callbacks.onPrevClick
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_player_skip_previous),
                        contentDescription = "Anterior"
                    )
                }

                IconButton(
                    modifier = Modifier
                        .focusRequester(playButtonFocusRequester),
                    onClick = callbacks.onPlayPauseToggle
                ) {
                    Icon(
                        painter = painterResource(
                            if (playbackState.isPlaying) {
                                R.drawable.ic_player_pause
                            } else {
                                R.drawable.ic_player_play
                            }
                        ),
                        contentDescription = if (playbackState.isPlaying) "Pausar" else "Reproducir"
                    )
                }

                IconButton(
                    onClick = callbacks.onNextClick
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_player_skip_next),
                        contentDescription = "Siguiente"
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

            }

            if (!headerInfo.isLive) {
                Text(
                    modifier = Modifier
                        .padding(start = 8.dp, bottom = 4.dp),
                    text = "${formatTime(playbackState.currentPosition)} / ${formatTime(playbackState.totalDuration)}",
                    style = MaterialTheme.typography.labelMedium,
                    color = Color.White.copy(0.7f)
                )
            } else {
                Row(
                    modifier = Modifier
                        .background(Color.Red, RoundedCornerShape(4.dp))
                        .padding(horizontal = 8.dp, vertical = 2.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Box(
                        modifier = Modifier
                            .size(if (platform == Platform.MOBILE) 6.dp else 8.dp)
                            .background(Color.White, CircleShape)
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = "EN VIVO",
                        color = Color.White,
                        style = if (platform == Platform.MOBILE) {
                            MaterialTheme.typography.bodySmall
                        } else {
                            MaterialTheme.typography.bodyMedium
                        },
                        fontWeight = FontWeight.Bold
                    )

                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {

                IconButton(
                    onClick = callbacks.onSettingsClick
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_subtitles),
                        contentDescription = "Subtítulos"
                    )
                }

                IconButton(
                    onClick = callbacks.onSettingsClick
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_settings),
                        contentDescription = "Configuración"
                    )
                }

            }

        }

    }

}