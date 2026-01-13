package com.jycra.filmaico.feature.movie

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.focus.FocusRequester
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jycra.filmaico.core.navigation.Platform
import com.jycra.filmaico.core.ui.util.focus.BrowseFocusCallbacks
import kotlinx.coroutines.android.awaitFrame

@Composable
fun MovieRoute(
    viewModel: MovieViewModel = hiltViewModel(),
    platform: Platform,
    contentPadding: PaddingValues = PaddingValues(),
    onFocusLeft: () -> Unit = {},
    contentFocusBeacon: FocusRequester? = null,
    onMovieClick: (String) -> Unit,
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        awaitFrame()
        viewModel.onScreenResumed()
    }

    LaunchedEffect(key1 = Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is MovieUiEffect.NavigateToDetail -> {
                    onMovieClick(effect.movieId)
                }
            }
        }
    }

    MovieScreen(
        uiState = uiState,
        platform = platform,
        contentPadding = contentPadding,
        browseFocusState = viewModel.browseFocusState,
        browseFocusCallbacks = BrowseFocusCallbacks(
            onFocusConsumed = viewModel::markInitialFocusConsumed,
            onFocusRestored = viewModel::markFocusRestored,
            onFocusLeft = { carouselIndex, contentIndex ->
                viewModel.saveFocusPosition(carouselIndex, contentIndex)
                onFocusLeft()
            },
            onBeaconReceived = {
                viewModel.onScreenResumed()
            }
        ),
        contentFocusBeacon = contentFocusBeacon,
        onEvent = viewModel::onEvent
    )

}