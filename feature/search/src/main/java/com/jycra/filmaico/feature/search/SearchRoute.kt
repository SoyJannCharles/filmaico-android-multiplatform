package com.jycra.filmaico.feature.search

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.focus.FocusRequester
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jycra.filmaico.core.device.Platform
import com.jycra.filmaico.core.ui.util.focus.MediaFocusCallbacks
import com.jycra.filmaico.domain.media.model.MediaType
import kotlinx.coroutines.android.awaitFrame

@Composable
fun SearchRoute(
    viewModel: SearchViewModel = hiltViewModel(),
    platform: Platform,
    contentPadding: PaddingValues = PaddingValues(),
    onFocusLeft: () -> Unit = {},
    contentFocusBeacon: FocusRequester? = null,
    onPlayAsset: (mediaType: MediaType, assetId: String) -> Unit,
    onOpenDetail: (mediaType: MediaType, assetId: String) -> Unit
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val searchFieldFocusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is SearchUiEffect.OpenDetail -> {
                    onOpenDetail(effect.mediaType, effect.containerId)
                }
                is SearchUiEffect.PlayAsset -> {
                    onPlayAsset(effect.mediaType, effect.assetId)
                }
            }
        }
    }

    SearchScreen(
        platform = platform,
        uiState = uiState,
        contentPadding = contentPadding,
        onRequestBrowseFocus = viewModel::requestInitialBrowseFocus,
        mediaFocusState = viewModel.mediaFocusState,
        mediaFocusCallbacks = MediaFocusCallbacks(
            onFocusConsumed = viewModel::markInitialFocusConsumed,
            onFocusRestored = viewModel::markFocusRestored,
            onFocusLeft = { carouselIndex, contentIndex ->
                viewModel.saveFocusPosition(carouselIndex, contentIndex)
                onFocusLeft()
            },
            onBeaconReceived = {
                val hasHistory = viewModel.mediaFocusState.lastFocusedCarouselId != null

                if (hasHistory) {
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