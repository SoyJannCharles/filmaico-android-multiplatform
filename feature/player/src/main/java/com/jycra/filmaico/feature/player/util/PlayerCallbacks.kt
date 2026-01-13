package com.jycra.filmaico.feature.player.util

data class PlayerCallbacks(
    val onPlayPauseToggle: () -> Unit,
    val onSeekTo: (Long) -> Unit,
    val onForwardClick: () -> Unit,
    val onRewindClick: () -> Unit,
    val onNextClick: () -> Unit,
    val onPrevClick: () -> Unit,
    val onBackClick: () -> Unit,
    val onSettingsClick: () -> Unit,
    val onInteraction: () -> Unit = {}
)