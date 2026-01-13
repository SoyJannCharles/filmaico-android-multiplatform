package com.jycra.filmaico.feature.search.component.topbar

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
import com.jycra.filmaico.core.navigation.Platform
import com.jycra.filmaico.core.ui.feature.browse.BrowseCarousel
import com.jycra.filmaico.core.ui.feature.browse.model.UiBrowseCarousel
import com.jycra.filmaico.core.ui.util.focus.BrowseFocusCallbacks
import com.jycra.filmaico.core.ui.util.focus.BrowseFocusState
import com.jycra.filmaico.feature.search.SearchUiEvent
import com.jycra.filmaico.feature.search.SearchUiState

@Composable
fun SearchResults(
    uiState: SearchUiState,
    onEvent: (SearchUiEvent) -> Unit,
    platform: Platform,
    contentPadding: PaddingValues,
    innerPadding: PaddingValues,
    browseFocusState: BrowseFocusState?,
    browseFocusCallbacks: BrowseFocusCallbacks?,
    searchFieldFocusRequester: FocusRequester,
) {

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
        contentPadding = innerPadding
    ) {

        itemsIndexed(
            items = uiState.carousels,
            key = { _, carousel -> carousel.carouselIndex }
        ) { index, carousel ->

            val cardModifier = if (platform == Platform.TV) {

                val isTopRow = index == 0
                val isBottomRow = index == uiState.carousels.lastIndex

                Modifier.focusProperties {
                    if (isTopRow) up = searchFieldFocusRequester
                    if (isBottomRow) down = FocusRequester.Cancel
                }

            } else Modifier

            BrowseCarousel(
                modifier = cardModifier,
                platform = platform,
                orientation = carousel.orientation,
                contentPadding = when (platform) {
                    Platform.MOBILE -> PaddingValues(horizontal = 16.dp)
                    Platform.TV -> contentPadding
                },
                carousel = UiBrowseCarousel(
                    id = carousel.carouselIndex.toString(),
                    title = carousel.title,
                    content = carousel.items
                ),
                carouselIndex = carousel.carouselIndex,
                browseFocusState = browseFocusState,
                browseFocusCallbacks = browseFocusCallbacks,
                onContentClick = { contentId, carouselIdx, contentIdx ->
                    when (carousel.contentType) {
                        "channel" -> onEvent(
                            SearchUiEvent.OnChannelClick(
                                contentId,
                                carouselIdx,
                                contentIdx
                            )
                        )
                        "movie" -> onEvent(
                            SearchUiEvent.OnMovieClick(
                                contentId,
                                carouselIdx,
                                contentIdx
                            )
                        )
                        "serie" -> onEvent(
                            SearchUiEvent.OnSerieClick(
                                contentId,
                                carouselIdx,
                                contentIdx
                            )
                        )
                        "anime" -> onEvent(
                            SearchUiEvent.OnAnimeClick(
                                contentId,
                                carouselIdx,
                                contentIdx
                            )
                        )
                    }
                }
            )

        }

    }

}