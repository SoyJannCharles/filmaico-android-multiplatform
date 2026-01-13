package com.jycra.filmaico.feature.search

import android.util.Log
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.focus.FocusRequester
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jycra.filmaico.core.navigation.Platform
import com.jycra.filmaico.core.ui.util.focus.BrowseFocusCallbacks
import kotlinx.coroutines.android.awaitFrame

@Composable
fun SearchRoute(
    viewModel: SearchViewModel = hiltViewModel(),
    platform: Platform,
    contentPadding: PaddingValues = PaddingValues(),
    onFocusLeft: () -> Unit = {},
    contentFocusBeacon: FocusRequester? = null,
    onNavigateToPlayer: (String, String) -> Unit,
    onNavigateToDetail: (String, String) -> Unit
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val searchFieldFocusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        awaitFrame()
        viewModel.onScreenResumed()
    }

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is SearchUiEffect.NavigateToPlayer -> {
                    onNavigateToPlayer(effect.contentType, effect.contentId)
                }
                is SearchUiEffect.NavigateToDetail -> {
                    onNavigateToDetail(effect.contentType, effect.contentId)
                }
            }
        }
    }

    SearchScreen(
        platform = platform,
        uiState = uiState,
        contentPadding = contentPadding,
        onRequestBrowseFocus = viewModel::requestInitialBrowseFocus,
        browseFocusState = viewModel.browseFocusState,
        browseFocusCallbacks = BrowseFocusCallbacks(
            onFocusConsumed = viewModel::markInitialFocusConsumed,
            onFocusRestored = viewModel::markFocusRestored,
            onFocusLeft = { carouselIndex, contentIndex ->
                viewModel.saveFocusPosition(carouselIndex, contentIndex)
                onFocusLeft()
            },
            onBeaconReceived = {
                if (viewModel.browseFocusState.lastFocusedContentIndex != null
                    && viewModel.browseFocusState.lastFocusedCarouselIndex != null
                ) {
                    viewModel.onScreenResumed()
                } else {
                    searchFieldFocusRequester.requestFocus()
                }
            }
        ),
        searchFieldFocusRequester = searchFieldFocusRequester,
        contentFocusBeacon = contentFocusBeacon,
        onEvent = viewModel::onEvent
    )

}