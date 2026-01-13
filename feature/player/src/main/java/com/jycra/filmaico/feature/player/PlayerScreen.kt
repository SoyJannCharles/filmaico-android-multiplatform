package com.jycra.filmaico.feature.player

import android.content.pm.ActivityInfo
import android.util.Log
import android.view.KeyEvent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import com.jycra.filmaico.core.navigation.Platform
import com.jycra.filmaico.core.player.VideoQuality
import com.jycra.filmaico.core.ui.SystemUiController
import com.jycra.filmaico.feature.player.components.controls.ControlLayer
import com.jycra.filmaico.feature.player.components.settings.SettingsMenu
import com.jycra.filmaico.feature.player.components.settings.SettingsMenuState
import com.jycra.filmaico.feature.player.model.VideoMetadata
import com.jycra.filmaico.feature.player.util.PlayerCallbacks
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive

@Composable
fun PlayerScreen(
    uiState: PlayerUiState,
    platform: Platform,
    isVisible: Boolean,
    shouldMountPlayer: Boolean,
    exoPlayer: ExoPlayer,
    onPlayerViewReady: () -> Unit = {},
    onGetVideoQualities: () -> List<VideoQuality>,
    onVideoQualityChange: (VideoQuality) -> Unit,
    onNavigateBack: () -> Unit
) {

    SystemUiController(
        isImmersive = true,
        keepScreenOn = true,
        orientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
    )

    when (uiState) {
        is PlayerUiState.Loading -> {
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

                    // Texto del paso actual
                    Text(
                        text = uiState.step, // <--- Accedemos al mensaje aquí
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                        textAlign = TextAlign.Center
                    )

                    // Opcional: Un texto más pequeño para pedir paciencia
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Por favor espere",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                }
            }
        }

        is PlayerUiState.Success -> {
            if (shouldMountPlayer) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .alpha(if (isVisible) 1f else 0f)
                ) {
                    PlayerScreenMixto(
                        platform = platform,
                        exoPlayer = exoPlayer,
                        headerInfo = uiState.headerInfo,
                        onPlayerViewReady = onPlayerViewReady,
                        onGetVideoQualities = onGetVideoQualities,
                        onVideoQualityChange = onVideoQualityChange,
                        onNavigateBack = onNavigateBack
                    )
                }
            }
        }

        is PlayerUiState.Error -> {

        }
    }

}

@Composable
private fun PlayerScreenMixto(
    platform: Platform = Platform.MOBILE,
    exoPlayer: ExoPlayer,
    headerInfo: VideoMetadata,
    onPlayerViewReady: () -> Unit = {},
    onGetVideoQualities: () -> List<VideoQuality>,
    onVideoQualityChange: (VideoQuality) -> Unit,
    onNavigateBack: () -> Unit
) {

    var controlsState by remember { mutableStateOf(ControlsState()) }
    var playbackState by remember { mutableStateOf(PlaybackState()) }
    var qualityState by remember { mutableStateOf(QualityState()) }

    var playerView by remember { mutableStateOf<PlayerView?>(null) }
    var hasNotifiedReady by remember { mutableStateOf(false) }

    val backgroundFocusRequester = remember { FocusRequester() }
    val menuFocusRequester = remember { FocusRequester() }

    DisposableEffect(exoPlayer) {

        val listener = object : Player.Listener {

            override fun onIsPlayingChanged(playing: Boolean) {
                playbackState = playbackState.copy(isPlaying = playing)
            }

            override fun onPlaybackStateChanged(state: Int) {
                if (state == Player.STATE_READY) {
                    playbackState = playbackState.copy(
                        totalDuration = exoPlayer.duration.coerceAtLeast(0L)
                    )
                }
            }

        }

        exoPlayer.addListener(listener)

        onDispose {
            exoPlayer.removeListener(listener)
        }

    }

    LaunchedEffect(
        controlsState.isVisible,
        playbackState.isPlaying,
        controlsState.menuState,
        controlsState.lastInteractionTime
    ) {
        if (controlsState.isVisible &&
            playbackState.isPlaying &&
            controlsState.menuState == SettingsMenuState.NONE
        ) {
            delay(5000)
            controlsState = controlsState.copy(isVisible = false)
        }
    }

    LaunchedEffect(controlsState.isVisible) {
        if (!controlsState.isVisible) {
            delay(50)
            backgroundFocusRequester.requestFocus()
        }
    }

    LaunchedEffect(controlsState.menuState) {
        if (controlsState.menuState != SettingsMenuState.NONE) {
            delay(50)
            try {
                menuFocusRequester.requestFocus()
            } catch (e: Exception) {
                Log.e("PlayerScreen", "Failed to focus menu", e)
            }
        }
    }

    LaunchedEffect(playbackState.isPlaying) {
        while (isActive && playbackState.isPlaying) {
            playbackState = playbackState.copy(
                currentPosition = exoPlayer.currentPosition
            )
            delay(500)
        }
    }

    val handleUserInteract = remember {
        {
            controlsState = controlsState.copy(
                isVisible = true,
                lastInteractionTime = System.currentTimeMillis()
            )
        }
    }

    val handlePlayPause = remember(exoPlayer) {
        {
            if (exoPlayer.isPlaying) exoPlayer.pause() else exoPlayer.play()
            handleUserInteract()
        }
    }

    val handleSeek = remember(exoPlayer) {
        { offset: Long ->
            exoPlayer.seekTo((exoPlayer.currentPosition + offset).coerceAtLeast(0))
            handleUserInteract()
        }
    }

    val handleSeekTo = remember(exoPlayer) {
        { position: Long ->
            exoPlayer.seekTo(position)
            handleUserInteract()
        }
    }

    val handleSettings = remember {
        {
            qualityState = qualityState.copy(
                qualities = onGetVideoQualities(),
                currentQuality = qualityState.currentQuality
                    ?: onGetVideoQualities().firstOrNull { it.isAuto }
            )
            controlsState = controlsState.copy(menuState = SettingsMenuState.MAIN)
            handleUserInteract()
        }
    }

    LaunchedEffect(playerView) {
        playerView?.let {
            if (!hasNotifiedReady) {
                hasNotifiedReady = true
                onPlayerViewReady()
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .then(
                if (platform == Platform.MOBILE) {
                    Modifier
                        .clickable {
                            controlsState = controlsState.copy(
                                isVisible = !controlsState.isVisible,
                                lastInteractionTime = System.currentTimeMillis()
                            )
                        }
                } else {
                    Modifier
                        .focusRequester(backgroundFocusRequester)
                        .focusable()
                        .onPreviewKeyEvent { keyEvent ->
                            handleKeyEvent(
                                keyEvent = keyEvent,
                                isControlsVisible = controlsState.isVisible,
                                onShowControls = handleUserInteract
                            )
                        }
                }
            )
    ) {

        AndroidView(
            modifier = Modifier
                .fillMaxSize(),
            factory = { ctx ->
                PlayerView(ctx).apply {
                    player = exoPlayer
                    useController = false
                    isFocusable = false
                    isClickable = false
                }
            },
            update = { view ->
                playerView = view
            }
        )

        AnimatedVisibility(
            modifier = Modifier
                .fillMaxSize(),
            visible = controlsState.isVisible,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            ControlLayer(
                platform = platform,
                headerInfo = headerInfo,
                playbackState = playbackState,
                callbacks = PlayerCallbacks(
                    onPlayPauseToggle = handlePlayPause,
                    onSeekTo = handleSeekTo,
                    onBackClick = onNavigateBack,
                    onSettingsClick = handleSettings,
                    onForwardClick = { handleSeek(10_000) },
                    onRewindClick = { handleSeek(-10_000) },
                    onNextClick = handleUserInteract,
                    onPrevClick = handleUserInteract,
                    onInteraction = handleUserInteract
                )
            )
        }

    }

    SettingsMenu(
        visible = controlsState.menuState != SettingsMenuState.NONE,
        menuState = controlsState.menuState,
        qualityState = qualityState,
        focusRequester = menuFocusRequester,
        platform = Platform.TV,
        onMenuStateChange = { newState ->
            controlsState = controlsState.copy(menuState = newState)
        },
        onQualityChange = { quality ->
            onVideoQualityChange(quality)
            qualityState = qualityState.copy(currentQuality = quality)
            controlsState = controlsState.copy(menuState = SettingsMenuState.NONE)
        },
        onDismiss = {
            controlsState = controlsState.copy(menuState = SettingsMenuState.NONE)
        }
    )

    SideEffect {
        if (!hasNotifiedReady  && playerView != null) {
            playerView?.post {
                if (!hasNotifiedReady ) {
                    hasNotifiedReady  = true
                    onPlayerViewReady()
                }
            }
        }
    }

}

private fun handleKeyEvent(
    keyEvent: androidx.compose.ui.input.key.KeyEvent,
    isControlsVisible: Boolean,
    onShowControls: () -> Unit
): Boolean {
    if (keyEvent.nativeKeyEvent.action == KeyEvent.ACTION_DOWN) {
        when (keyEvent.nativeKeyEvent.keyCode) {
            KeyEvent.KEYCODE_DPAD_CENTER,
            KeyEvent.KEYCODE_ENTER,
            KeyEvent.KEYCODE_NUMPAD_ENTER,
            KeyEvent.KEYCODE_DPAD_UP,
            KeyEvent.KEYCODE_DPAD_DOWN,
            KeyEvent.KEYCODE_DPAD_LEFT,
            KeyEvent.KEYCODE_DPAD_RIGHT -> {
                if (!isControlsVisible) {
                    onShowControls()
                    return true
                }
            }
        }
    }
    return false
}