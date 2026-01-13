package com.jycra.filmaico.core.ui.feature.detail.content

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
import com.jycra.filmaico.core.ui.theme.LocalAppDimens

@Composable
fun ContentCover(
    modifier: Modifier = Modifier,
    coverUrl: String,
    contentDescription: String
) {

    val dimens = LocalAppDimens.current

    Surface(
        modifier = modifier
            .width(dimens.coverWidth)
            .height(dimens.coverHeight),
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