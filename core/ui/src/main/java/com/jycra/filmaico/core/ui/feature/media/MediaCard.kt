package com.jycra.filmaico.core.ui.feature.media

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import com.jycra.filmaico.core.device.Platform
import com.jycra.filmaico.core.ui.feature.media.model.UiMedia
import com.jycra.filmaico.core.ui.feature.media.util.variant.MediaCardVariant
import com.jycra.filmaico.core.ui.feature.media.util.dimens.getMediaCardDimensions
import com.jycra.filmaico.core.ui.feature.media.util.focus.FocusRestorationHandler
import com.jycra.filmaico.core.ui.feature.media.util.focus.InitialFocusHandler
import com.jycra.filmaico.core.ui.util.focus.MediaFocusCallbacks
import com.jycra.filmaico.core.ui.util.focus.MediaFocusState

@Composable
fun MediaCard(
    modifier: Modifier = Modifier,
    platform: Platform,
    variant: MediaCardVariant,
    media: UiMedia,
    carouselIndex: Int = 0,
    contentIndex: Int = 0,
    mediaFocusState: MediaFocusState? = null,
    mediaFocusCallbacks: MediaFocusCallbacks? = null,
    onContentClick: () -> Unit
) {

    val dimensions = getMediaCardDimensions(variant)

    val focusRequester = if (platform == Platform.TV) remember { FocusRequester() } else null

    if (platform == Platform.TV && focusRequester != null &&
        mediaFocusState != null && mediaFocusCallbacks != null) {
        InitialFocusHandler(focusRequester, carouselIndex, contentIndex, mediaFocusState, mediaFocusCallbacks)
        FocusRestorationHandler(focusRequester, carouselIndex, contentIndex, mediaFocusState, mediaFocusCallbacks)
    }

    when (variant) {
        MediaCardVariant.POSTER_VERTICAL, MediaCardVariant.BACKDROP_HORIZONTAL -> {
            MediaGridCard(
                modifier = modifier,
                platform = platform,
                variant = variant,
                media = media,
                dimensions = dimensions,
                focusRequester = focusRequester,
                carouselIndex = carouselIndex,
                contentIndex = contentIndex,
                mediaFocusCallbacks = mediaFocusCallbacks,
                onContentClick = onContentClick
            )
        }
        MediaCardVariant.THUMBNAIL_STANDARD -> {
            MediaThumbnailStandardCard(
                modifier = modifier,
                platform = platform,
                media = media,
                variant = variant,
                dimensions = dimensions,
                carouselIndex = carouselIndex,
                contentIndex = contentIndex,
                mediaFocusCallbacks = mediaFocusCallbacks,
                focusRequester = focusRequester,
                onContentClick = onContentClick
            )
        }
        MediaCardVariant.THUMBNAIL_COMPACT_ROW -> {
            MediaThumbnailCompactCard(
                modifier = modifier,
                platform = platform,
                media = media,
                variant = variant,
                dimensions = dimensions,
                carouselIndex = carouselIndex,
                contentIndex = contentIndex,
                mediaFocusCallbacks = mediaFocusCallbacks,
                focusRequester = focusRequester,
                onContentClick = onContentClick
            )
        }
    }

}