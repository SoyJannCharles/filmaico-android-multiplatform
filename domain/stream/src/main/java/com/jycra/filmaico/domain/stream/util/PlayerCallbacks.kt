package com.jycra.filmaico.domain.stream.util

data class PlayerCallbacks(
    val onPlayPauseToggle: () -> Unit,
    val onNextClick: () -> Unit,
    val onPrevClick: () -> Unit,
    val onSeekTo: (Long) -> Unit,
    val onForwardClick: () -> Unit,
    val onRewindClick: () -> Unit,
    val onToggleSaved: () -> Unit,
    val onSettingsClick: () -> Unit,
    val onInteraction: () -> Unit = {},
    val onBackClick: () -> Unit,
)