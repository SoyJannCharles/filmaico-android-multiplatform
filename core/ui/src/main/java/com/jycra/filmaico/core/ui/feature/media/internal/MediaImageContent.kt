package com.jycra.filmaico.core.ui.feature.media.internal

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.jycra.filmaico.core.ui.component.image.BlurredImage
import com.jycra.filmaico.core.ui.feature.media.model.UiMedia
import com.jycra.filmaico.core.ui.feature.media.util.dimens.MediaCardDimensions
import com.jycra.filmaico.domain.media.model.MediaType

@Composable
fun MediaImageContent(media: UiMedia, dimensions: MediaCardDimensions) {

    if (media.mediaType == MediaType.CHANNEL) {

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {

            BlurredImage(
                imageUrl = media.imageUrl,
                blurRadius = 32.dp,
                zoom = 1.92f
            )

            AsyncImage(
                modifier = Modifier.size(dimensions.width / 1.5f, dimensions.height / 1.5f),
                model = media.imageUrl,
                contentDescription = null,
                contentScale = ContentScale.Fit
            )

        }

    } else {

        AsyncImage(
            modifier = Modifier.fillMaxSize(),
            model = media.imageUrl,
            contentDescription = media.name,
            contentScale = ContentScale.Crop
        )

    }

}