package com.jycra.filmaico.feature.panel

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.focus.FocusRequester
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jycra.filmaico.core.device.Platform
import com.jycra.filmaico.core.ui.util.focus.MediaFocusCallbacks
import kotlinx.coroutines.android.awaitFrame

@Composable
fun PanelRoute(
    viewModel: PanelViewModel = hiltViewModel(),
    platform: Platform,
    contentPadding: PaddingValues = PaddingValues(),
    contentFocusBeacon: FocusRequester? = null,
    onFocusLeft: () -> Unit = {},
    onSignOut: () -> Unit
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        awaitFrame()
        viewModel.onScreenResumed()
    }

    LaunchedEffect(Unit) {
        viewModel.effect.collect { event ->
            when (event) {
                is PanelUiEffect.SignOut -> {
                    onSignOut()
                }
            }
        }
    }

    AccountScreen(
        uiState = uiState,
        platform = platform,
        contentPadding = contentPadding,
        mediaFocusState = viewModel.mediaFocusState,
        mediaFocusCallbacks = MediaFocusCallbacks(
            onFocusConsumed = viewModel::markInitialFocusConsumed,
            onFocusRestored = viewModel::markFocusRestored,
            onFocusLeft = { carouselIndex, _ ->
                viewModel.saveFocusPosition(carouselIndex)
                onFocusLeft()
            },
            onBeaconReceived = {
                viewModel.onScreenResumed()
            }
        ),
        contentFocusBeacon = contentFocusBeacon,
        onEvent = { event -> viewModel.onEvent(event) }
    )

}