package com.jycra.filmaico.feature.player

import com.jycra.filmaico.core.player.VideoQuality
import com.jycra.filmaico.feature.player.components.settings.SettingsMenuState
import com.jycra.filmaico.feature.player.model.VideoMetadata

sealed interface PlayerUiState {
    data class Loading(val step: String = "Cargando...") : PlayerUiState
    data class Success(val headerInfo: VideoMetadata) : PlayerUiState
    data class Error(val message: String) : PlayerUiState
}

data class ControlsState(
    val isVisible: Boolean = false,
    val lastInteractionTime: Long = System.currentTimeMillis(),
    val menuState: SettingsMenuState = SettingsMenuState.NONE
)

data class PlaybackState(
    val isPlaying: Boolean = true,
    val currentPosition: Long = 0L,
    val totalDuration: Long = 0L
)

data class QualityState(
    val qualities: List<VideoQuality> = emptyList(),
    val currentQuality: VideoQuality? = null
)

sealed interface PlayerUiEvent {
    object NavigateBack : PlayerUiEvent
    data class NavigateBackWithError(val message: String) : PlayerUiEvent
}