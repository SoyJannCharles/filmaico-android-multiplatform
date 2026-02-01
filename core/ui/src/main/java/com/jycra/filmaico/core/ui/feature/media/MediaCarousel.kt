package com.jycra.filmaico.core.ui.feature.media

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusProperties
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.jycra.filmaico.core.device.Platform
import com.jycra.filmaico.core.ui.feature.media.model.UiMediaCarousel
import com.jycra.filmaico.core.ui.feature.media.util.variant.MediaCardVariant
import com.jycra.filmaico.core.ui.feature.media.util.orientation.CarouselOrientation
import com.jycra.filmaico.core.ui.util.focus.MediaFocusCallbacks
import com.jycra.filmaico.core.ui.util.focus.MediaFocusState
import com.jycra.filmaico.domain.media.model.MediaType

@Composable
fun MediaCarousel(
    modifier: Modifier = Modifier,
    platform: Platform,
    contentPadding: PaddingValues = PaddingValues(horizontal = 16.dp),
    orientation: CarouselOrientation = CarouselOrientation.HORIZONTAL,
    carousel: UiMediaCarousel,
    carouselIndex: Int = 0,
    mediaFocusState: MediaFocusState? = null,
    mediaFocusCallbacks: MediaFocusCallbacks? = null,
    onContentClick: (mediaId: String, mediaType: MediaType, carouselIndex: Int, contentIndex: Int) -> Unit
) {

    val lazyListState = rememberLazyListState()

    Column {

        if (carousel.hasTitle) {

            Text(
                modifier = Modifier.padding(
                    horizontal = when (platform) {
                        Platform.MOBILE -> 16.dp
                        Platform.TV -> contentPadding.calculateStartPadding(LayoutDirection.Ltr)
                    }
                ),
                text = carousel.title ?: "",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(16.dp))

        }

        when (orientation) {
            CarouselOrientation.VERTICAL -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    state = lazyListState,
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = if (platform == Platform.MOBILE)
                        PaddingValues(
                            top = contentPadding.calculateTopPadding(),
                            start = 16.dp,
                            end = 16.dp,
                            bottom = 16.dp
                        )
                    else
                        contentPadding
                ) {

                    itemsIndexed(
                        items = carousel.items,
                        key = { _, media -> media.id }
                    ) { contentIndex, media ->

                        MediaCard(
                            modifier = modifier,
                            platform = platform,
                            variant = MediaCardVariant.getVariantByCarouselId(
                                carouselId = carousel.id,
                                platform = platform,
                                fallback = media.variant
                            ),
                            media = media,
                            carouselIndex = carouselIndex,
                            contentIndex = contentIndex,
                            mediaFocusState = mediaFocusState,
                            mediaFocusCallbacks = mediaFocusCallbacks,
                            onContentClick = {
                                onContentClick(media.id, media.mediaType, carouselIndex, contentIndex)
                            }
                        )

                    }

                }
            }
            CarouselOrientation.HORIZONTAL -> {
                LazyRow(
                    modifier = Modifier.then(
                        if (platform == Platform.TV) Modifier.focusProperties { canFocus = false }
                        else Modifier
                    ),
                    state = lazyListState,
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = if (platform == Platform.MOBILE)
                        PaddingValues(horizontal = 16.dp)
                    else
                        contentPadding
                ) {

                    itemsIndexed(
                        items = carousel.items,
                        key = { _, media -> media.id }
                    ) { contentIndex, media ->

                        MediaCard(
                            modifier = modifier,
                            platform = platform,
                            variant = MediaCardVariant.getVariantByCarouselId(
                                carouselId = carousel.id,
                                platform = platform,
                                fallback = media.variant
                            ),
                            media = media,
                            carouselIndex = carouselIndex,
                            contentIndex = contentIndex,
                            mediaFocusState = mediaFocusState,
                            mediaFocusCallbacks = mediaFocusCallbacks,
                            onContentClick = {
                                onContentClick(media.id, media.mediaType, carouselIndex, contentIndex)
                            }
                        )

                    }

                }
            }
        }

    }

}