package com.jycra.filmaico.core.ui.feature.detail.internal

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.jycra.filmaico.core.device.Platform
import com.jycra.filmaico.core.ui.feature.detail.util.dimens.getCoverDimensions

@Composable
fun MediaCover(
    modifier: Modifier = Modifier,
    platform: Platform,
    coverUrl: String,
    contentDescription: String
) {

    val dimensions = getCoverDimensions(platform = platform)

    Surface(
        modifier = modifier
            .width(dimensions.width)
            .height(dimensions.height),
        shape = RoundedCornerShape(12.dp)
    ) {

        AsyncImage(
            modifier = Modifier
                .fillMaxSize(),
            model = ImageRequest.Builder(LocalContext.current)
                .data(coverUrl)
                .crossfade(true)
                .build(),
            contentDescription = contentDescription,
            contentScale = ContentScale.Crop
        )

    }

}