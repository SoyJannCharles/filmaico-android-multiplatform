package com.jycra.filmaico.feature.saves

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusProperties
import androidx.compose.ui.unit.dp
import com.jycra.filmaico.core.device.Platform
import com.jycra.filmaico.core.ui.feature.media.MediaCarousel
import com.jycra.filmaico.core.ui.feature.media.model.UiMediaCarousel
import com.jycra.filmaico.core.ui.util.focus.FocusBeacon
import com.jycra.filmaico.core.ui.util.focus.MediaFocusCallbacks
import com.jycra.filmaico.core.ui.util.focus.MediaFocusState
import com.jycra.filmaico.domain.media.model.MediaType

@Composable
fun SavesScreen(
    uiState: SavesUiState,
    platform: Platform,
    contentPadding: PaddingValues,
    mediaFocusState: MediaFocusState,
    mediaFocusCallbacks: MediaFocusCallbacks,
    contentFocusBeacon: FocusRequester? = null,
    onEvent: (SavesUiEvent) -> Unit
) {

    when (uiState) {
        is SavesUiState.Loading -> {

        }
        is SavesUiState.Success -> {
            Screen(
                platform = platform,
                contentPadding = contentPadding,
                carousels = uiState.carousels,
                mediaFocusState = mediaFocusState,
                mediaFocusCallbacks = mediaFocusCallbacks,
                contentFocusBeacon = contentFocusBeacon,
                onOpenDetail = { mediaId, mediaType, carouselIndex, contentIndex ->
                    onEvent(SavesUiEvent.OpenDetail(mediaId, mediaType, carouselIndex, contentIndex))
                },
                onPlayAsset = { assetId, mediaType, carouselIndex, contentIndex ->
                    onEvent(SavesUiEvent.PlayAsset(assetId, mediaType, carouselIndex, contentIndex))
                }
            )
        }
        is SavesUiState.Error -> {

        }
    }

}

@Composable
fun Screen(
    platform: Platform,
    contentPadding: PaddingValues,
    carousels: List<UiMediaCarousel>,
    mediaFocusState: MediaFocusState? = null,
    mediaFocusCallbacks: MediaFocusCallbacks? = null,
    contentFocusBeacon: FocusRequester? = null,
    onOpenDetail: (mediaId: String, mediaType: MediaType, carouselIndex: Int, contentIndex: Int) -> Unit,
    onPlayAsset: (assetId: String, mediaType: MediaType, carouselIndex: Int, contentIndex: Int) -> Unit
) {

    val lazyColumnState = rememberLazyListState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .then(
                if (platform == Platform.TV) {
                    Modifier.focusProperties { canFocus = false }
                } else Modifier
            ),
        state = lazyColumnState,
        verticalArrangement = Arrangement.spacedBy(
            if (platform == Platform.TV) 8.dp else 16.dp
        ),
        contentPadding = when (platform) {
            Platform.MOBILE -> contentPadding
            Platform.TV -> PaddingValues(top = 32.dp, bottom = 32.dp)
        }
    ) {

        itemsIndexed(
            items = carousels,
            key = { _, carousel -> carousel.id },
            contentType = { _, carousel -> carousel.title }
        ) { carouselIndex, carousel ->

            val cardModifier = if (platform == Platform.TV) {

                val isTopRow = carouselIndex == 0
                val isBottomRow = carouselIndex == carousels.lastIndex

                Modifier.focusProperties {
                    if (isTopRow) up = FocusRequester.Cancel
                    if (isBottomRow) down = FocusRequester.Cancel
                }

            } else Modifier

            MediaCarousel(
                modifier = cardModifier,
                platform = platform,
                contentPadding = contentPadding,
                carousel = carousel,
                carouselIndex = carouselIndex,
                mediaFocusState = mediaFocusState,
                mediaFocusCallbacks = mediaFocusCallbacks,
                onContentClick = { mediaId, mediaType, carouselIndex, contentIndex ->
                    when (mediaType) {
                        MediaType.CHANNEL, MediaType.MOVIE -> {
                            onPlayAsset(mediaId, mediaType, carouselIndex, contentIndex)
                        }
                        else -> {
                            onOpenDetail(mediaId, mediaType, carouselIndex, contentIndex)
                        }
                    }
                }
            )

        }

    }

    if (platform == Platform.TV && contentFocusBeacon != null &&
        mediaFocusState != null && mediaFocusCallbacks != null) {
        FocusBeacon(
            focusRequester = contentFocusBeacon,
            mediaFocusCallbacks = mediaFocusCallbacks
        )
    }

}