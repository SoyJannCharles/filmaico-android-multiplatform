package com.jycra.filmaico.feature.movie

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.focus.FocusRequester
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jycra.filmaico.core.device.Platform
import com.jycra.filmaico.core.ui.util.focus.MediaFocusCallbacks
import com.jycra.filmaico.domain.media.model.MediaType
import kotlinx.coroutines.android.awaitFrame

@Composable
fun MovieRoute(
    viewModel: MovieViewModel = hiltViewModel(),
    platform: Platform,
    contentPadding: PaddingValues = PaddingValues(),
    onFocusLeft: () -> Unit = {},
    contentFocusBeacon: FocusRequester? = null,
    onOpenDetail: (containerId: String) -> Unit,
    onPlayAsset: (mediaType: MediaType, assetId: String) -> Unit
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val extractionState by viewModel.extractionState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        awaitFrame()
        viewModel.onScreenResumed()
    }

    LaunchedEffect(key1 = Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is MovieUiEffect.OpenDetail -> {
                    onOpenDetail(effect.containerId)
                }
                is MovieUiEffect.PlayAsset -> {
                    onPlayAsset(effect.mediaType, effect.assetId)
                }
            }
        }
    }

    MovieScreen(
        uiState = uiState,
        extractionState = extractionState,
        platform = platform,
        contentPadding = contentPadding,
        mediaFocusState = viewModel.mediaFocusState,
        mediaFocusCallbacks = MediaFocusCallbacks(
            onFocusConsumed = viewModel::markInitialFocusConsumed,
            onFocusRestored = viewModel::markFocusRestored,
            onFocusLeft = { carouselIndex, contentIndex ->
                viewModel.saveFocusPosition(carouselIndex, contentIndex)
                onFocusLeft()
            },
            onBeaconReceived = {
                viewModel.onScreenResumed()
            },
            onPreloadRequested = { carouselIndex, contentIndex ->
                viewModel.preloadAsset(carouselIndex, contentIndex)
            }
        ),
        contentFocusBeacon = contentFocusBeacon,
        onEvent = viewModel::onEvent
    )

}