package com.jycra.filmaico.core.ui.feature.media

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.unit.dp
import com.jycra.filmaico.core.device.Platform
import com.jycra.filmaico.core.ui.feature.media.internal.MediaImageContent
import com.jycra.filmaico.core.ui.feature.media.internal.MediaMetadata
import com.jycra.filmaico.core.ui.feature.media.internal.MediaProgressBar
import com.jycra.filmaico.core.ui.feature.media.model.UiMedia
import com.jycra.filmaico.core.ui.feature.media.util.MediaCardContainer
import com.jycra.filmaico.core.ui.feature.media.util.dimens.MediaCardDimensions
import com.jycra.filmaico.core.ui.feature.media.util.variant.MediaCardVariant
import com.jycra.filmaico.core.ui.util.focus.MediaFocusCallbacks

@Composable
fun MediaThumbnailCompactCard(
    modifier: Modifier = Modifier,
    platform: Platform,
    media: UiMedia,
    variant: MediaCardVariant,
    dimensions: MediaCardDimensions,
    carouselIndex: Int,
    contentIndex: Int,
    mediaFocusCallbacks: MediaFocusCallbacks? = null,
    focusRequester: FocusRequester? = null,
    onContentClick: () -> Unit
) {

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

        Row(
            modifier = Modifier
                .fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            Box(
                modifier = Modifier
                    .width(dimensions.width)
                    .height(dimensions.height)
                    .clip(RoundedCornerShape(12.dp))
            ) {

                MediaImageContent(media, dimensions)

                if (media.lastPosition > 0 && !media.isFinished) {
                    MediaProgressBar(
                        modifier = Modifier.align(Alignment.BottomCenter),
                        lastPosition = media.lastPosition,
                        durationInMs = media.durationInMs
                    )
                }

            }

            MediaMetadata(media, variant)

        }

    }

    /*Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLowest
        ),
        onClick = onContentClick
    ) {

        Row(verticalAlignment = Alignment.CenterVertically) {

            AsyncImage(
                modifier = Modifier
                    .width(dimensions.width)
                    .height(dimensions.height)
                    .clip(RoundedCornerShape(8.dp)),
                model = media.imageUrl,
                contentDescription = media.name,
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {

                Text(
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onSurface,
                    text = media.name,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                if (variant != MediaCardVariant.POSTER_VERTICAL) {
                    if (media.label.isNotBlank()) {
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = media.label,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.72f),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }

            }

        }

    }*/

}