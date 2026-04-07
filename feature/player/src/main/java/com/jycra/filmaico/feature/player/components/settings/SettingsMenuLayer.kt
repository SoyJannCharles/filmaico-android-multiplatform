package com.jycra.filmaico.feature.player.components.settings

import androidx.activity.compose.BackHandler
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
import com.jycra.filmaico.domain.stream.model.metadata.AudioMetadata
import com.jycra.filmaico.core.player.model.Quality
import com.jycra.filmaico.domain.media.model.stream.Stream
import com.jycra.filmaico.feature.player.AudioState
import com.jycra.filmaico.feature.player.ProviderState
import com.jycra.filmaico.feature.player.QualityState

@Composable
fun SettingsMenu(
    visible: Boolean,
    menuState: SettingsMenuState,
    qualityState: QualityState,
    providerState: ProviderState,
    audioState: AudioState,
    focusRequester: FocusRequester,
    onMenuStateChange: (SettingsMenuState) -> Unit,
    onQualityChange: (Quality) -> Unit,
    onProviderChange: (Stream) -> Unit,
    onAudioChange: (AudioMetadata) -> Unit,
    onDismiss: () -> Unit
) {

    BackHandler(enabled = visible) {
        if (menuState != SettingsMenuState.MAIN) {
            onMenuStateChange(SettingsMenuState.MAIN)
        } else {
            onDismiss()
        }
    }

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
                                onProviderClick = { onMenuStateChange(SettingsMenuState.PROVIDER) },
                                onAudioClick = { onMenuStateChange(SettingsMenuState.AUDIO) },
                                onDismiss = onDismiss
                            )
                        }
                        SettingsMenuState.QUALITY -> {
                            QualitySettingsSheet(
                                qualities = qualityState.availableQualities,
                                currentQuality = qualityState.currentQuality,
                                focusRequester = focusRequester,
                                onQualitySelected = onQualityChange
                            )
                        }
                        SettingsMenuState.PROVIDER -> {
                            ProviderSettingsSheet(
                                providers = providerState.availableProviders,
                                currentProvider = providerState.currentProvider,
                                analysis = providerState.analysis,
                                focusRequester = focusRequester,
                                onProviderSelected = onProviderChange
                            )
                        }
                        SettingsMenuState.AUDIO -> {
                            AudioSettingsSheet(
                                audioMetadata = audioState.availableAudioMetadata,
                                currentAudioMetadata = audioState.currentAudioMetadata,
                                focusRequester = focusRequester,
                                onAudioSelected = onAudioChange
                            )
                        }
                        else -> { /* Nada */ }
                    }

                }

            }

        }

    }

}