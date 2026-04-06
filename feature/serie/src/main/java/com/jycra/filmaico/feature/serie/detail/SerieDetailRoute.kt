package com.jycra.filmaico.feature.serie.detail

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jycra.filmaico.core.device.Platform
import com.jycra.filmaico.core.ui.util.focus.MediaFocusCallbacks
import com.jycra.filmaico.domain.media.model.MediaType
import kotlinx.coroutines.android.awaitFrame

@Composable
fun SerieDetailRoute(
    viewModel: SerieDetailViewModel = hiltViewModel(),
    platform: Platform,
    onPlayAsset: (mediaType: MediaType, assetId: String) -> Unit,
    onNavigateBack: () -> Unit
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        awaitFrame()
        viewModel.onScreenResumed()
    }

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is SerieDetailUiEffect.PlayAsset -> onPlayAsset(effect.mediaType, effect.assetId)
                is SerieDetailUiEffect.NavigateBack -> onNavigateBack()
            }
        }
    }

    SerieDetailScreen(
        uiState = uiState,
        platform = platform,
        mediaFocusState = viewModel.mediaFocusState,
        mediaFocusCallbacks = MediaFocusCallbacks(
            onFocusConsumed = viewModel::markInitialFocusConsumed,
            onFocusRestored = viewModel::markFocusRestored,
            onPreloadRequested = { _, contentIndex ->
                viewModel.preloadAsset(contentIndex)
            }
        ),
        onEvent = viewModel::onEvent
    )

}