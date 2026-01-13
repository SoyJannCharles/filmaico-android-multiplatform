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
import com.jycra.filmaico.core.navigation.Platform

@Composable
fun PlayerRoute(
    viewModel: PlayerViewModel = hiltViewModel(),
    platform: Platform,
    onNavigateBack: (error: String?) -> Unit
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val isVisible by viewModel.isPlayerVisible.collectAsStateWithLifecycle()
    val shouldMountPlayer by viewModel.shouldMountPlayer.collectAsStateWithLifecycle()

    val lifecycleOwner = LocalLifecycleOwner.current

    BackHandler(enabled = true) {
        viewModel.onNavigateBack()
    }

    DisposableEffect(lifecycleOwner) {

        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_PAUSE -> {
                    viewModel.onPause()
                }
                Lifecycle.Event.ON_RESUME -> {
                    viewModel.onResume()
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
        viewModel.eventFlow.collect { event ->
            when (event) {
                is PlayerUiEvent.NavigateBack -> {
                    onNavigateBack(null)
                }
                is PlayerUiEvent.NavigateBackWithError -> {
                    onNavigateBack(event.message)
                }
            }
        }
    }

    PlayerScreen(
        uiState = uiState,
        platform = platform,
        isVisible = isVisible,
        shouldMountPlayer = shouldMountPlayer,
        exoPlayer = viewModel.playerManager.exoPlayer,
        onPlayerViewReady = { viewModel.onPlayerViewReady() },
        onGetVideoQualities = { viewModel.getAvailableQualities() },
        onVideoQualityChange = { quality -> viewModel.changeVideoQuality(quality) },
        onNavigateBack = { viewModel.onNavigateBack() }
    )

}