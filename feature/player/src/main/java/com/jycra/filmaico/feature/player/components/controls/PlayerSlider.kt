package com.jycra.filmaico.feature.player.components.controls

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Slider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.unit.dp
import com.jycra.filmaico.core.device.Platform
import kotlinx.coroutines.delay

@Composable
fun PlayerSlider(
    platform: Platform,
    currentPosition: Long,
    totalDuration: Long,
    isSeeking: Boolean,
    onPlayPauseToggle: () -> Unit,
    onSeekTo: (Long) -> Unit,
    focusRequester: FocusRequester,
    playButtonFocusRequester: FocusRequester
) {

    var localSeekPosition by remember { mutableLongStateOf(-1L) }

    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()

    val thumbRadius by animateDpAsState(
        targetValue = if (isFocused) 6.dp else 4.dp,
        label = "ThumbRadius"
    )

    LaunchedEffect(localSeekPosition) {
        if (localSeekPosition != -1L) {
            delay(500)
            onSeekTo(localSeekPosition)
        }
    }

    LaunchedEffect(isSeeking) {
        if (!isSeeking) {
            localSeekPosition = -1L
        }
    }

    val displayPosition = when {
        localSeekPosition != -1L -> localSeekPosition
        isSeeking -> currentPosition
        else -> currentPosition
    }

    val sliderValue = remember(displayPosition, totalDuration) {
        if (totalDuration > 0) {
            (displayPosition.toFloat() / totalDuration).coerceIn(0f, 1f)
        } else 0f
    }

    Slider(
        modifier = Modifier
            .fillMaxWidth()
            .then(
                if (platform == Platform.TV) {
                    Modifier
                        .focusRequester(focusRequester)
                        .onPreviewKeyEvent { keyEvent ->
                            if (platform == Platform.TV && keyEvent.type == KeyEventType.KeyDown) {
                                when (keyEvent.key) {
                                    Key.DirectionLeft -> {
                                        val currentBase = if (localSeekPosition != -1L) localSeekPosition else currentPosition
                                        localSeekPosition = (currentBase - 10_000L).coerceAtLeast(0L)
                                        true
                                    }
                                    Key.DirectionRight -> {
                                        val currentBase = if (localSeekPosition != -1L) localSeekPosition else currentPosition
                                        localSeekPosition = (currentBase + 10_000L).coerceAtMost(totalDuration)
                                        true
                                    }
                                    Key.DirectionDown -> {
                                        playButtonFocusRequester.requestFocus()
                                        true
                                    }
                                    Key.DirectionUp -> {
                                        true
                                    }
                                    Key.Enter -> {
                                        onPlayPauseToggle()
                                        true
                                    }
                                    else -> false
                                }
                            } else {
                                false
                            }
                        }
                } else Modifier
            ),
        value = sliderValue,
        onValueChange = { newValue ->
            localSeekPosition = (newValue * totalDuration).toLong()
        },
        interactionSource = interactionSource,
        thumb = {
            Canvas(modifier = Modifier.size(48.dp)) {

                drawCircle(
                    color = Color.White,
                    radius = thumbRadius.toPx(),
                    center = center,
                    style = Fill
                )

                if (isFocused) {
                    drawCircle(
                        color = Color.White.copy(alpha = 0.2f),
                        radius = (thumbRadius + 4.dp).toPx(),
                        center = center
                    )
                }

            }
        },
        track = { sliderState ->
            Canvas(modifier = Modifier.fillMaxWidth()) {

                val trackHeight = 4.dp.toPx()
                val progressEndX = size.width * sliderState.value

                drawLine(
                    color = Color.White.copy(alpha = 0.3f),
                    start = Offset(0f, center.y),
                    end = Offset(size.width, center.y),
                    strokeWidth = trackHeight,
                    cap = StrokeCap.Round
                )

                drawLine(
                    color = Color.Red,
                    start = Offset(0f, center.y),
                    end = Offset(progressEndX, center.y),
                    strokeWidth = trackHeight,
                    cap = StrokeCap.Round
                )

            }
        }
    )

}