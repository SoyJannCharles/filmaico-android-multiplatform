package com.jycra.filmaico.feature.player

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jycra.filmaico.core.device.Platform

@Composable
fun PlayerRoute(
    viewModel: PlayerViewModel = hiltViewModel(),
    platform: Platform,
    onNavigateBack: (error: String?) -> Unit
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val lifecycleOwner = LocalLifecycleOwner.current

    BackHandler(enabled = true) {
        viewModel.onEvent(PlayerUiEvent.OnBackClick)
    }

    DisposableEffect(lifecycleOwner) {

        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_PAUSE -> {
                    viewModel.onEvent(PlayerUiEvent.OnLifecyclePause)
                }
                Lifecycle.Event.ON_RESUME -> {
                    viewModel.onEvent(PlayerUiEvent.OnLifecycleResume)
                }
                else -> {}
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }

    }

    LaunchedEffect(Unit) {
        viewModel.effect.collect { event ->
            when (event) {
                is PlayerUiEffect.NavigateBack -> {
                    onNavigateBack(null)
                }
                is PlayerUiEffect.ShowError -> {
                    onNavigateBack(event.message)
                }
            }
        }
    }

    PlayerScreen(
        uiState = uiState,
        platform = platform,
        exoPlayer = viewModel.playerManager.exoPlayer,
        onEvent = { event -> viewModel.onEvent(event) }
    )

}