package com.jycra.filmaico.core.ui.feature.media

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import com.jycra.filmaico.core.device.Platform
import com.jycra.filmaico.core.ui.feature.media.util.MediaCardContainer
import com.jycra.filmaico.core.ui.feature.media.internal.MediaMetadata
import com.jycra.filmaico.core.ui.feature.media.internal.MediaImageContent
import com.jycra.filmaico.core.ui.feature.media.internal.MediaProgressBar
import com.jycra.filmaico.core.ui.feature.media.model.UiMedia
import com.jycra.filmaico.core.ui.feature.media.util.dimens.MediaCardDimensions
import com.jycra.filmaico.core.ui.feature.media.util.variant.MediaCardVariant
import com.jycra.filmaico.core.ui.util.focus.MediaFocusCallbacks

@Composable
fun MediaGridCard(
    modifier: Modifier,
    platform: Platform,
    variant: MediaCardVariant,
    media: UiMedia,
    dimensions: MediaCardDimensions,
    focusRequester: FocusRequester?,
    carouselIndex: Int,
    contentIndex: Int,
    mediaFocusCallbacks: MediaFocusCallbacks?,
    onContentClick: () -> Unit
) {

    Column(modifier = Modifier.width(dimensions.width)) {

        MediaCardContainer(
            modifier = modifier,
            platform = platform,
            dimensions = dimensions,
            focusRequester = focusRequester,
            carouselIndex = carouselIndex,
            contentIndex = contentIndex,
            mediaFocusCallbacks = mediaFocusCallbacks,
            onContentClick = onContentClick
        ) {

            Box(modifier = Modifier.fillMaxSize()) {

                MediaImageContent(media, dimensions)

                if (media.lastPosition > 0 && !media.isFinished) {
                    MediaProgressBar(
                        modifier = Modifier.align(Alignment.BottomCenter),
                        lastPosition = media.lastPosition,
                        durationInMs = media.durationInMs
                    )
                }

            }

        }

        MediaMetadata(media, variant)

    }

}