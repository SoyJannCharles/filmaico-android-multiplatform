package com.jycra.filmaico.feature.player.components.controls

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.unit.dp
import com.jycra.filmaico.core.navigation.Platform
import com.jycra.filmaico.core.ui.theme.color.Gradient
import com.jycra.filmaico.feature.player.PlaybackState
import com.jycra.filmaico.feature.player.model.VideoMetadata
import com.jycra.filmaico.feature.player.util.PlayerCallbacks
import kotlinx.coroutines.delay

@Composable
fun ControlLayer(
    modifier: Modifier = Modifier,
    platform: Platform,
    headerInfo: VideoMetadata,
    playbackState: PlaybackState,
    callbacks: PlayerCallbacks
) {

    val sliderFocusRequester = remember { FocusRequester() }
    val playButtonFocusRequester = remember { FocusRequester() }

    InitialFocusHandler(
        isLive = headerInfo.isLive,
        sliderFocusRequester = sliderFocusRequester,
        playButtonFocusRequester = playButtonFocusRequester
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(brush = Gradient.playerControlGradient())
            .padding(vertical = 16.dp)
            .then(
                if (platform == Platform.TV) {
                    Modifier
                        .onPreviewKeyEvent {
                            callbacks.onInteraction()
                            false
                        }
                } else Modifier
            )
    ) {

        PlayerHeader(
            modifier = Modifier
                .align(Alignment.TopStart),
            platform = platform,
            headerInfo = headerInfo,
            onBackClick = callbacks.onBackClick
        )

        if (platform == Platform.MOBILE) {
            CenterControls(
                modifier = Modifier.align(Alignment.Center),
                headerInfo = headerInfo,
                isPlaying = playbackState.isPlaying,
                callbacks = callbacks
            )
        }

        BottomControls(
            modifier = Modifier.align(Alignment.BottomCenter),
            platform = platform,
            headerInfo = headerInfo,
            playbackState = playbackState,
            callbacks = callbacks,
            sliderFocusRequester = sliderFocusRequester,
            playButtonFocusRequester = playButtonFocusRequester
        )

    }

}

@Composable
private fun InitialFocusHandler(
    isLive: Boolean,
    sliderFocusRequester: FocusRequester,
    playButtonFocusRequester: FocusRequester
) {
    LaunchedEffect(Unit) {
        delay(50)
        try {
            if (isLive) {
                playButtonFocusRequester.requestFocus()
            } else {
                sliderFocusRequester.requestFocus()
            }
        } catch (e: Exception) {

        }
    }
}

// Función auxiliar para formatear milisegundos a MM:SS
fun formatTime(ms: Long): String {
    val totalSeconds = ms / 1000
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60
    return String.format("%02d:%02d", minutes, seconds)
}