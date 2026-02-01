package com.jycra.filmaico.feature.movie

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusProperties
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.jycra.filmaico.core.device.Platform
import com.jycra.filmaico.core.ui.feature.media.MediaCarousel
import com.jycra.filmaico.core.ui.feature.media.model.UiMediaCarousel
import com.jycra.filmaico.core.ui.feature.media.util.variant.MediaCardVariant
import com.jycra.filmaico.core.ui.util.shimmer.ShimmerMediaCard
import com.jycra.filmaico.core.ui.util.focus.MediaFocusCallbacks
import com.jycra.filmaico.core.ui.util.focus.MediaFocusState
import com.jycra.filmaico.core.ui.util.focus.FocusBeacon
import com.jycra.filmaico.core.ui.util.shimmer.rememberShimmerBrush
import com.jycra.filmaico.domain.media.model.MediaType

@Composable
fun MovieScreen(
    uiState: MovieUiState,
    platform: Platform,
    contentPadding: PaddingValues,
    mediaFocusState: MediaFocusState,
    mediaFocusCallbacks: MediaFocusCallbacks,
    contentFocusBeacon: FocusRequester? = null,
    onEvent: (MovieUiEvent) -> Unit
) {

    when (uiState) {
        is MovieUiState.Loading -> {
            LoadingScreen(
                platform = platform,
                contentPadding = contentPadding
            )
        }
        is MovieUiState.Success -> {
            Screen(
                platform = platform,
                contentPadding = contentPadding,
                carousels = uiState.carousels,
                mediaFocusState = mediaFocusState,
                mediaFocusCallbacks = mediaFocusCallbacks,
                contentFocusBeacon = contentFocusBeacon,
                onOpenDetail = { containerId, carouselIndex, contentIndex ->
                    onEvent(MovieUiEvent.OpenDetail(containerId, carouselIndex, contentIndex))
                },
                onPlayAsset = { assetId, mediaType, carouselIndex, contentIndex ->
                    onEvent(MovieUiEvent.PlayAsset(assetId, mediaType, carouselIndex, contentIndex))
                }
            )
        }
        is MovieUiState.Error -> {

        }
    }

}

@Composable
private fun LoadingScreen(
    platform: Platform,
    contentPadding: PaddingValues
) {

    LazyColumn(
        contentPadding = when (platform) {
            Platform.MOBILE -> contentPadding
            Platform.TV -> PaddingValues(
                start = contentPadding.calculateStartPadding(LayoutDirection.Ltr),
                top = 32.dp,
                bottom = 32.dp
            )
        }
    ) {

        items(3) {

            Column(modifier = Modifier.padding(bottom = 24.dp)) {

                Box(
                    modifier = Modifier
                        .padding(start = 16.dp)
                        .width(120.dp)
                        .height(20.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(rememberShimmerBrush())
                )

                Spacer(modifier = Modifier.height(16.dp))

                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(5) {
                        ShimmerMediaCard(platform = platform, variant = MediaCardVariant.POSTER_VERTICAL)
                    }
                }

            }

        }

    }

}

@Composable
private fun Screen(
    platform: Platform,
    contentPadding: PaddingValues,
    carousels: List<UiMediaCarousel>,
    mediaFocusState: MediaFocusState? = null,
    mediaFocusCallbacks: MediaFocusCallbacks? = null,
    contentFocusBeacon: FocusRequester? = null,
    onOpenDetail: (mediaId: String, carouselIndex: Int, contentIndex: Int) -> Unit,
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
                    if (carousel.id == "continue_watching")
                        onPlayAsset(mediaId, mediaType, carouselIndex, contentIndex)
                    else
                        onOpenDetail(mediaId, carouselIndex, contentIndex)
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