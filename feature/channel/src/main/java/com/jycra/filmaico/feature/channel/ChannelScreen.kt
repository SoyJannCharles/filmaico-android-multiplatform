package com.jycra.filmaico.feature.channel

import android.util.Log
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
import com.jycra.filmaico.core.navigation.Platform
import com.jycra.filmaico.core.ui.feature.browse.BrowseCarousel
import com.jycra.filmaico.core.ui.feature.browse.model.UiBrowseCarousel
import com.jycra.filmaico.core.ui.feature.browse.model.UiBrowseCarouselItem
import com.jycra.filmaico.core.ui.feature.browse.util.CardOrientation
import com.jycra.filmaico.domain.channel.model.ChannelCarousel
import com.jycra.filmaico.core.ui.util.focus.BrowseFocusCallbacks
import com.jycra.filmaico.core.ui.util.focus.BrowseFocusState
import com.jycra.filmaico.core.ui.util.focus.FocusBeacon
import com.jycra.filmaico.domain.channel.model.localizedName
import com.jycra.filmaico.domain.channel.model.localizedTitle

@Composable
fun ChannelScreen(
    uiState: ChannelUiState,
    platform: Platform,
    contentPadding: PaddingValues,
    browseFocusState: BrowseFocusState,
    browseFocusCallbacks: BrowseFocusCallbacks,
    contentFocusBeacon: FocusRequester? = null,
    onEvent: (ChannelUiEvent) -> Unit
) {

    when (uiState) {
        is ChannelUiState.Loading -> {

        }
        is ChannelUiState.Success -> {
            Screen(
                platform = platform,
                contentPadding = contentPadding,
                carousels = uiState.carousels,
                browseFocusState = browseFocusState,
                browseFocusCallbacks = browseFocusCallbacks,
                contentFocusBeacon = contentFocusBeacon,
                onChannelClick = { channelId, carouselIndex, channelIndex ->
                    onEvent(
                        ChannelUiEvent.OnChannelClick(
                            channelId,
                            carouselIndex,
                            channelIndex
                        )
                    )
                }
            )
        }
        is ChannelUiState.Error -> {
            Log.d("ChannelScreen", "Error: ${uiState.message}")
        }
    }

}

@Composable
private fun Screen(
    platform: Platform,
    contentPadding: PaddingValues,
    carousels: List<ChannelCarousel>,
    browseFocusState: BrowseFocusState? = null,
    browseFocusCallbacks: BrowseFocusCallbacks? = null,
    contentFocusBeacon: FocusRequester? = null,
    onChannelClick: (String, Int, Int) -> Unit,
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

            val carouselModifier = if (platform == Platform.TV) {

                val isTopRow = carouselIndex == 0
                val isBottomRow = carouselIndex == carousels.lastIndex

                Modifier.focusProperties {
                    if (isTopRow) up = FocusRequester.Cancel
                    if (isBottomRow) down = FocusRequester.Cancel
                }

            } else Modifier

            BrowseCarousel(
                modifier = carouselModifier,
                platform = platform,
                contentPadding = contentPadding,
                orientation = CardOrientation.HORIZONTAL,
                carousel = UiBrowseCarousel(
                    id = carousel.id,
                    title = carousel.localizedTitle,
                    content = carousel.channels.map { channel ->
                        UiBrowseCarouselItem(
                            id = channel.id,
                            name = channel.localizedName,
                            imageUrl = channel.iconUrl
                        )
                    }
                ),
                carouselIndex = carouselIndex,
                browseFocusState = browseFocusState,
                browseFocusCallbacks = browseFocusCallbacks,
                onContentClick = onChannelClick
            )

        }

    }

    if (platform == Platform.TV && contentFocusBeacon != null &&
        browseFocusState != null && browseFocusCallbacks != null) {
        FocusBeacon(
            focusRequester = contentFocusBeacon,
            browseFocusCallbacks = browseFocusCallbacks
        )
    }

}