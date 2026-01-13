package com.jycra.filmaico.core.ui.feature.browse

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.jycra.filmaico.core.ui.component.image.BlurredImage
import com.jycra.filmaico.core.ui.feature.browse.util.CardDimensions
import com.jycra.filmaico.core.ui.feature.browse.util.CardOrientation

@Composable
fun CardContent(
    orientation: CardOrientation,
    imageUrl: String,
    name: String,
    dimensions: CardDimensions
) {

    when (orientation) {

        CardOrientation.VERTICAL -> {
            AsyncImage(
                modifier = Modifier.fillMaxSize(),
                model = ImageRequest.Builder(LocalContext.current)
                    .data(imageUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = name,
                contentScale = ContentScale.Crop
            )
        }

        CardOrientation.HORIZONTAL -> {

            val iconWidth = dimensions.width / 1.5f
            val iconHeight = dimensions.height / 1.5f

            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {

                BlurredImage(
                    modifier = Modifier.fillMaxSize(),
                    imageUrl = imageUrl,
                    blurRadius = 32.dp,
                    zoom = 1.92f
                )

                AsyncImage(
                    modifier = Modifier
                        .width(iconWidth)
                        .height(iconHeight),
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(imageUrl)
                        .crossfade(true)
                        .build(),
                    contentDescription = name,
                    contentScale = ContentScale.Fit
                )

            }

        }

    }

}