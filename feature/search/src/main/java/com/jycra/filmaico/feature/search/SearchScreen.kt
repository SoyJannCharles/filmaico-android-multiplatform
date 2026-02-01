package com.jycra.filmaico.feature.search

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusProperties
import androidx.compose.ui.unit.dp
import com.jycra.filmaico.core.device.Platform
import com.jycra.filmaico.core.ui.feature.media.MediaCarousel
import com.jycra.filmaico.core.ui.util.focus.MediaFocusCallbacks
import com.jycra.filmaico.core.ui.util.focus.MediaFocusState
import com.jycra.filmaico.core.ui.util.focus.FocusBeacon
import com.jycra.filmaico.domain.media.model.MediaType
import com.jycra.filmaico.feature.search.component.topbar.SearchTopbar

@Composable
fun SearchScreen(
    platform: Platform,
    uiState: SearchUiState,
    contentPadding: PaddingValues,
    onRequestBrowseFocus: () -> Unit,
    mediaFocusState: MediaFocusState,
    mediaFocusCallbacks: MediaFocusCallbacks,
    searchFieldFocusRequester: FocusRequester,
    contentFocusBeacon: FocusRequester? = null,
    onEvent: (SearchUiEvent) -> Unit
) {

    Screen(
        platform = platform,
        uiState = uiState,
        contentPadding = contentPadding,
        onRequestBrowseFocus = onRequestBrowseFocus,
        mediaFocusState = mediaFocusState,
        mediaFocusCallbacks = mediaFocusCallbacks,
        searchFieldFocusRequester = searchFieldFocusRequester,
        contentFocusBeacon = contentFocusBeacon,
        onQueryChange = { query ->
            onEvent(SearchUiEvent.OnQueryChange(query))
        },
        onOpenDetail = { containerId, mediaType, carouselId, carouselIndex, contentIndex ->
            onEvent(SearchUiEvent.OpenDetail(containerId, mediaType, carouselId, carouselIndex, contentIndex))
        },
        onPlayAsset = { assetId, mediaType, carouselId, carouselIndex, contentIndex ->
            onEvent(SearchUiEvent.PlayAsset(assetId, mediaType, carouselId, carouselIndex, contentIndex))
        }
    )

}

@Composable
private fun Screen(
    platform: Platform,
    uiState: SearchUiState,
    contentPadding: PaddingValues,
    onRequestBrowseFocus: () -> Unit,
    mediaFocusState: MediaFocusState? = null,
    mediaFocusCallbacks: MediaFocusCallbacks? = null,
    searchFieldFocusRequester: FocusRequester,
    contentFocusBeacon: FocusRequester? = null,
    onQueryChange: (query: String) -> Unit,
    onOpenDetail: (containerId: String, mediaType: MediaType, carouselId: String, carouselIndex: Int, contentIndex: Int) -> Unit,
    onPlayAsset: (assetId: String, mediaType: MediaType, carouselId: String, carouselIndex: Int, contentIndex: Int) -> Unit
) {

    SearchTopbar(
        uiState = uiState,
        platform = platform,
        contentPadding = contentPadding,
        onRequestBrowseFocus = onRequestBrowseFocus,
        onQueryChange = onQueryChange,
        searchFieldFocusRequester = searchFieldFocusRequester
    ) { innerPadding ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .then(
                    if (platform == Platform.TV) {
                        Modifier.focusProperties { canFocus = false }
                    } else Modifier
                ),
            verticalArrangement = Arrangement.spacedBy(
                space = if (platform == Platform.TV) 8.dp else 16.dp
            ),
            contentPadding = when (platform) {
                Platform.MOBILE -> innerPadding
                Platform.TV -> PaddingValues(
                    top = innerPadding.calculateTopPadding(),
                    bottom = 32.dp
                )
            }
        ) {

            itemsIndexed(
                items = uiState.results,
                key = { _, carousel -> carousel.id }
            ) { index, carousel ->

                val cardModifier = if (platform == Platform.TV) {

                    val isTopRow = index == 0
                    val isBottomRow = index == uiState.results.lastIndex

                    Modifier.focusProperties {
                        if (isTopRow) up = searchFieldFocusRequester
                        if (isBottomRow) down = FocusRequester.Cancel
                    }

                } else Modifier

                MediaCarousel(
                    modifier = cardModifier,
                    platform = platform,
                    contentPadding = contentPadding,
                    carousel = carousel,
                    carouselIndex = index,
                    mediaFocusState = mediaFocusState,
                    mediaFocusCallbacks = mediaFocusCallbacks,
                    onContentClick = { mediaId, mediaType, carouselIndex, contentIndex ->
                        when (mediaType) {
                            MediaType.CHANNEL -> onPlayAsset(mediaId, mediaType, carousel.id, carouselIndex, contentIndex)
                            else -> onOpenDetail(mediaId, mediaType, carousel.id, carouselIndex, contentIndex)
                        }
                    }
                )
            }

        }

    }

    if (platform == Platform.TV && contentFocusBeacon != null &&
        mediaFocusState != null && mediaFocusCallbacks != null
    ) {
        FocusBeacon(
            focusRequester = contentFocusBeacon,
            mediaFocusCallbacks = mediaFocusCallbacks
        )
    }

}