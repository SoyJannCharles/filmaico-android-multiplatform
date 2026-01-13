package com.jycra.filmaico.feature.serie.detail

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jycra.filmaico.core.navigation.Platform
import com.jycra.filmaico.core.ui.util.focus.BrowseFocusCallbacks
import kotlinx.coroutines.android.awaitFrame

@Composable
fun SerieDetailRoute(
    viewModel: SerieDetailViewModel = hiltViewModel(),
    platform: Platform,
    onNavigateToPlayer: (String) -> Unit,
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
                is SerieDetailUiEffect.NavigateToPlayer -> onNavigateToPlayer(effect.serieId)
                is SerieDetailUiEffect.NavigateBack -> onNavigateBack()
            }
        }
    }

    SerieDetailScreen(
        uiState = uiState,
        platform = platform,
        browseFocusState = viewModel.browseFocusState,
        browseFocusCallbacks = BrowseFocusCallbacks(
            onFocusConsumed = viewModel::markInitialFocusConsumed,
            onFocusRestored = viewModel::markFocusRestored
        ),
        onEvent = viewModel::onEvent
    )

}