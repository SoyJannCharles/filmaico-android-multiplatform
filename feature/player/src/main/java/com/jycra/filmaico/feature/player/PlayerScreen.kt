package com.jycra.filmaico.feature.player

import android.content.pm.ActivityInfo
import android.view.KeyEvent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.media3.exoplayer.ExoPlayer
import com.jycra.filmaico.core.device.Platform
import com.jycra.filmaico.core.ui.R
import com.jycra.filmaico.core.ui.SystemUiController
import com.jycra.filmaico.domain.media.util.PlayerCallbacks
import com.jycra.filmaico.feature.player.components.VideoPlayerView
import com.jycra.filmaico.feature.player.components.controls.ControlLayer
import com.jycra.filmaico.feature.player.components.settings.SettingsMenu
import com.jycra.filmaico.feature.player.components.settings.SettingsMenuState
import kotlinx.coroutines.android.awaitFrame

@Composable
fun PlayerScreen(
    uiState: PlayerUiState,
    platform: Platform,
    exoPlayer: ExoPlayer,
    onEvent: (event: PlayerUiEvent) -> Unit
) {

    SystemUiController(
        isImmersive = true,
        keepScreenOn = true,
        orientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
    )

    when (uiState) {
        is PlayerUiState.Loading -> {
            LoadingScreen(step = uiState.step)
        }
        is PlayerUiState.Success -> {
            Screen(
                platform = platform,
                exoPlayer = exoPlayer,
                uiState = uiState,
                onEvent = onEvent
            )
        }
        is PlayerUiState.Error -> {
            ErrorScreen(
                message = uiState.message,
                onRetryPlayback = { onEvent(PlayerUiEvent.OnRetryPlayback) }
            )
        }
        is PlayerUiState.Closing -> {

        }
    }

}

@Composable
private fun LoadingScreen(
    step: String
) {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.surfaceContainerLowest),
        contentAlignment = Alignment.Center
    ) {

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            CircularProgressIndicator(
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = step,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Por favor espere",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )

        }
    }

}

@Composable
private fun Screen(
    uiState: PlayerUiState.Success,
    platform: Platform,
    exoPlayer: ExoPlayer,
    onEvent: (event: PlayerUiEvent) -> Unit
) {

    val videoFocusRequester = remember { FocusRequester() }

    LaunchedEffect(uiState) {
        if (!uiState.controls.isVisible && uiState.controls.menuState == SettingsMenuState.NONE) {
            awaitFrame()
            videoFocusRequester.requestFocus()
        }
    }

    val menuFocusRequester = remember { FocusRequester() }

    LaunchedEffect(uiState.controls.menuState) {
        if (uiState.controls.menuState != SettingsMenuState.NONE) {
            menuFocusRequester.requestFocus()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .then(
                if (platform == Platform.MOBILE) {
                    Modifier
                        .clickable(
                            onClick = { onEvent(PlayerUiEvent.OnUserInteract(platform)) },
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        )
                } else {
                    Modifier
                        .focusRequester(videoFocusRequester)
                        .focusable()
                        .onPreviewKeyEvent { keyEvent ->
                            handleRemoteInput(
                                keyEvent = keyEvent,
                                isControlsVisible = uiState.controls.isVisible,
                                onShowControls = { onEvent(PlayerUiEvent.OnUserInteract(platform)) }
                            )
                        }
                }
            )
    ) {

        VideoPlayerView(
            exoPlayer = exoPlayer,
            onViewReady = { playerView -> onEvent(PlayerUiEvent.OnPlayerReady(playerView)) }
        )

        AnimatedVisibility(
            visible = uiState.controls.isVisible,
            enter = fadeIn(),
            exit = fadeOut()
        ) {

            ControlLayer(
                platform = platform,
                videoMetadata = uiState.videoMetadata,
                controlsState = uiState.controls,
                playbackState = uiState.playback,
                callbacks = PlayerCallbacks(
                    onPlayPauseToggle = { onEvent(PlayerUiEvent.OnPlayPauseToggle) },
                    onNextClick = { onEvent(PlayerUiEvent.OnNextClick) },
                    onPrevClick = { onEvent(PlayerUiEvent.OnPrevClick) },
                    onSeekTo = { pos -> onEvent(PlayerUiEvent.OnSeekTo(pos)) },
                    onForwardClick = { onEvent(PlayerUiEvent.OnSeekRelative(10_000)) },
                    onRewindClick = { onEvent(PlayerUiEvent.OnSeekRelative(-10_000)) },
                    onToggleSaved = { onEvent(PlayerUiEvent.OnToggleSaved) },
                    onSettingsClick = { onEvent(PlayerUiEvent.OnSettingsClick) },
                    onInteraction = { onEvent(PlayerUiEvent.OnUserInteract(platform)) },
                    onBackClick = { onEvent(PlayerUiEvent.OnBackClick) },
                )
            )

        }

        SettingsMenu(
            visible = uiState.controls.menuState != SettingsMenuState.NONE,
            menuState = uiState.controls.menuState,
            qualityState = uiState.quality,
            providerState = uiState.provider,
            audioState = uiState.audio,
            focusRequester = menuFocusRequester,
            onMenuStateChange = { newState -> onEvent(PlayerUiEvent.OnMenuNavigate(newState)) },
            onQualityChange = { quality -> onEvent(PlayerUiEvent.OnQualityChange(quality)) },
            onProviderChange = { provider -> onEvent(PlayerUiEvent.OnProviderChange(provider)) },
            onAudioChange = { audio -> onEvent(PlayerUiEvent.OnAudioChange(audio)) },
            onDismiss = { onEvent(PlayerUiEvent.OnMenuDismiss) }
        )

    }

}

@Composable
private fun ErrorScreen(
    message: String,
    onRetryPlayback: () -> Unit
) {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surfaceContainerLowest),
        contentAlignment = Alignment.Center
    ) {

        Column(
            modifier = Modifier
                .padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Icon(
                painter = painterResource(R.drawable.ic_error),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Ups! Hubo un problema",
                color = Color.White,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = message,
                color = Color.Gray,
                fontSize = 14.sp,
                lineHeight = 20.sp,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = onRetryPlayback,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .height(48.dp)
                    .widthIn(min = 140.dp)
            ) {
                Text(
                    text = "Reintentar",
                    fontWeight = FontWeight.SemiBold
                )
            }

        }

    }

}

private fun handleRemoteInput(
    keyEvent: androidx.compose.ui.input.key.KeyEvent,
    isControlsVisible: Boolean,
    onShowControls: () -> Unit
): Boolean {

    if (keyEvent.nativeKeyEvent.action != KeyEvent.ACTION_DOWN) return false

    return when (keyEvent.nativeKeyEvent.keyCode) {
        KeyEvent.KEYCODE_DPAD_CENTER,
        KeyEvent.KEYCODE_ENTER,
        KeyEvent.KEYCODE_NUMPAD_ENTER,
        KeyEvent.KEYCODE_DPAD_UP,
        KeyEvent.KEYCODE_DPAD_DOWN,
        KeyEvent.KEYCODE_DPAD_LEFT,
        KeyEvent.KEYCODE_DPAD_RIGHT,
        KeyEvent.KEYCODE_INFO -> {
            if (!isControlsVisible) {
                onShowControls()
                true
            } else {
                false
            }
        }
        else -> false
    }
}