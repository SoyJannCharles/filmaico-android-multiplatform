package com.jycra.filmaico.feature.player.components.controls

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.unit.dp
import com.jycra.filmaico.core.device.Platform
import com.jycra.filmaico.core.ui.theme.color.Gradient
import com.jycra.filmaico.domain.media.model.metadata.VideoMetadata
import com.jycra.filmaico.feature.player.ControlsState
import com.jycra.filmaico.feature.player.PlaybackState
import com.jycra.filmaico.feature.player.components.settings.SettingsMenuState
import com.jycra.filmaico.domain.stream.util.PlayerCallbacks

@Composable
fun ControlLayer(
    modifier: Modifier = Modifier,
    platform: Platform,
    videoMetadata: VideoMetadata,
    controlsState: ControlsState,
    playbackState: PlaybackState,
    callbacks: PlayerCallbacks
) {

    val sliderFocusRequester = remember { FocusRequester() }
    val playButtonFocusRequester = remember { FocusRequester() }

    val hasConsumedInitialFocus = remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        try {
            if (videoMetadata.isLive) {
                playButtonFocusRequester.requestFocus()
            } else {
                sliderFocusRequester.requestFocus()
            }
            hasConsumedInitialFocus.value = true
        } catch (e: Exception) {

        }
    }

    LaunchedEffect(controlsState.menuState) {
        if (controlsState.menuState == SettingsMenuState.NONE && hasConsumedInitialFocus.value) {
            if (videoMetadata.isLive) {
                playButtonFocusRequester.requestFocus()
            } else {
                sliderFocusRequester.requestFocus()
            }
        }
    }

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
                } else {
                    Modifier
                }
            )
    ) {

        if (playbackState.isBuffering) {
            CircularProgressIndicator(
                modifier = Modifier
                    .size(48.dp)
                    .align(alignment = Alignment.Center),
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        PlayerHeader(
            modifier = Modifier
                .align(Alignment.TopStart),
            platform = platform,
            metadata = videoMetadata,
            onBackClick = callbacks.onBackClick
        )

        if (platform == Platform.MOBILE) {
            CenterControls(
                modifier = Modifier.align(Alignment.Center),
                headerInfo = videoMetadata,
                isPlaying = playbackState.isPlaying,
                callbacks = callbacks
            )
        }

        BottomControls(
            modifier = Modifier.align(Alignment.BottomCenter),
            platform = platform,
            videoMetadata = videoMetadata,
            playbackState = playbackState,
            callbacks = callbacks,
            sliderFocusRequester = sliderFocusRequester,
            playButtonFocusRequester = playButtonFocusRequester
        )

    }

}