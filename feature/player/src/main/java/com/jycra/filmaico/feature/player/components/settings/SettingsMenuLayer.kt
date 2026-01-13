package com.jycra.filmaico.feature.player.components.settings

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.unit.dp
import com.jycra.filmaico.core.navigation.Platform
import com.jycra.filmaico.core.player.VideoQuality
import com.jycra.filmaico.feature.player.QualityState

@Composable
fun SettingsMenu(
    visible: Boolean,
    menuState: SettingsMenuState,
    qualityState: QualityState,
    focusRequester: FocusRequester,
    platform: Platform,
    onMenuStateChange: (SettingsMenuState) -> Unit,
    onQualityChange: (VideoQuality) -> Unit,
    onDismiss: () -> Unit
) {

    AnimatedVisibility(
        modifier = Modifier.fillMaxSize(),
        visible = visible,
        enter = slideInVertically(initialOffsetY = { it }) + fadeIn(animationSpec = tween(300)),
        exit = slideOutVertically(targetOffsetY = { it }) + fadeOut(animationSpec = tween(300))
    ) {

        Box(
            modifier = Modifier
                .fillMaxSize()
                .clickable { onDismiss() },
            contentAlignment = Alignment.BottomCenter
        ) {

            Surface(
                modifier = Modifier
                    .padding(bottom = 24.dp)
                    .widthIn(min = 300.dp, max = 400.dp)
                    .heightIn(max = 280.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .clickable { },
                color = MaterialTheme.colorScheme.surfaceContainerLowest,
                tonalElevation = 8.dp
            ) {

                Crossfade(
                    targetState = menuState,
                    label = "MenuNavigation",
                    animationSpec = tween(200)
                ) { state ->

                    when (state) {
                        SettingsMenuState.MAIN -> {
                            MainSettingsSheet(
                                focusRequester = focusRequester,
                                onQualityClick = { onMenuStateChange(SettingsMenuState.QUALITY) },
                                onSpeedClick = { onMenuStateChange(SettingsMenuState.SPEED) },
                                onSubtitlesClick = { onMenuStateChange(SettingsMenuState.SUBTITLES) }
                            )
                        }
                        SettingsMenuState.QUALITY -> {
                            QualitySettingsSheet(
                                qualities = qualityState.qualities,
                                currentQuality = qualityState.currentQuality,
                                focusRequester = focusRequester,
                                onQualitySelected = onQualityChange,
                                onBack = { onMenuStateChange(SettingsMenuState.MAIN) }
                            )
                        }
                        SettingsMenuState.SPEED -> {
                            // TODO: Implementar
                        }
                        SettingsMenuState.SUBTITLES -> {
                            // TODO: Implementar
                        }
                        else -> { /* Nada */ }
                    }

                }

            }

        }

    }

}