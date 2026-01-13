package com.jycra.filmaico.feature.player.components.controls

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Slider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
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
import com.jycra.filmaico.core.navigation.Platform

@Composable
fun PlayerSlider(
    platform: Platform,
    currentPosition: Long,
    totalDuration: Long,
    onPlayPauseToggle: () -> Unit,
    onSeekTo: (Long) -> Unit,
    focusRequester: FocusRequester,
    playButtonFocusRequester: FocusRequester
) {

    val sliderValue = remember(currentPosition, totalDuration) {
        if (totalDuration > 0) {
            (currentPosition.toFloat() / totalDuration).coerceIn(0f, 1f)
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
                                        val newPos = (currentPosition - 10_000L)
                                            .coerceAtLeast(0L)
                                        onSeekTo(newPos)
                                        true
                                    }
                                    Key.DirectionRight -> {
                                        val newPos = (currentPosition + 10_000L)
                                            .coerceAtMost(totalDuration)
                                        onSeekTo(newPos)
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
            val newPos = (newValue * totalDuration).toLong()
            onSeekTo(newPos)
        },
        thumb = {
            Canvas(modifier = Modifier.size(48.dp)) {
                drawCircle(
                    color = Color.White,
                    radius = 6.dp.toPx(),
                    center = center,
                    style = Fill
                )
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