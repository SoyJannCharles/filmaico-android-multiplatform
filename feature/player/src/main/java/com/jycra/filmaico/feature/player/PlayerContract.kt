package com.jycra.filmaico.feature.player

import android.view.TextureView
import com.jycra.filmaico.core.device.Platform
import com.jycra.filmaico.core.player.model.VideoQuality
import com.jycra.filmaico.domain.media.model.metadata.VideoMetadata
import com.jycra.filmaico.feature.player.components.settings.SettingsMenuState

sealed interface PlayerUiState {
    data class Loading(val step: String = "Cargando...") : PlayerUiState
    data class Success(
        val videoMetadata: VideoMetadata,
        val playback: PlaybackState,
        val controls: ControlsState,
        val quality: QualityState
    ) : PlayerUiState
    data class Error(val message: String) : PlayerUiState
    data object Closing : PlayerUiState
}

data class PlaybackState(
    val isPlaying: Boolean = true,
    val isBuffering: Boolean = false,
    val isSeeking: Boolean = false,
    val currentPosition: Long = 0L,
    val totalDuration: Long = 0L,
    val bufferedPercentage: Int = 0,
    val isLive: Boolean = false
)

data class ControlsState(
    val isVisible: Boolean = true,
    val menuState: SettingsMenuState = SettingsMenuState.NONE
)

data class QualityState(
    val qualities: List<VideoQuality> = emptyList(),
    val currentQuality: VideoQuality? = null
)

sealed interface PlayerUiEvent {
    // Ciclo de vida y Setup
    data class OnPlayerReady(val playerView: TextureView) : PlayerUiEvent
    data object OnLifecyclePause : PlayerUiEvent
    data object OnLifecycleResume : PlayerUiEvent
    data object OnRetryPlayback : PlayerUiEvent

    // Controles de Reproducción
    data object OnPlayPauseToggle : PlayerUiEvent
    data object OnNextClick : PlayerUiEvent
    data object OnPrevClick : PlayerUiEvent
    data class OnSeekTo(val position: Long) : PlayerUiEvent
    data class OnSeekRelative(val offset: Long) : PlayerUiEvent // +10s / -10s

    data object OnToggleSaved : PlayerUiEvent

    // Interacción con la UI (Gestión de controles)
    data class OnUserInteract(val platform: Platform) : PlayerUiEvent // Muestra controles + Resetea timer
    data object OnDismissControls : PlayerUiEvent // Oculta forzosamente

    // Menú de Configuración
    data object OnSettingsClick : PlayerUiEvent
    data class OnMenuNavigate(val state: SettingsMenuState) : PlayerUiEvent
    data class OnQualityChange(val quality: VideoQuality) : PlayerUiEvent
    data object OnMenuDismiss : PlayerUiEvent

    // Navegación
    data object OnBackClick : PlayerUiEvent
}

sealed interface PlayerUiEffect {
    data object NavigateBack : PlayerUiEffect
    data class ShowError(val message: String) : PlayerUiEffect
}