package com.jycra.filmaico.core.ui.feature.media.util.dimens

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.jycra.filmaico.core.ui.feature.media.util.variant.MediaCardVariant

data class MediaCardDimensions(
    val width: Dp,
    val height: Dp
)

@Composable
fun getMediaCardDimensions(
    variant: MediaCardVariant
): MediaCardDimensions {
    return when (variant) {
        MediaCardVariant.POSTER_VERTICAL -> MediaCardDimensions(
            width = 128.dp,
            height = 192.dp
        )
        MediaCardVariant.BACKDROP_HORIZONTAL -> MediaCardDimensions(
            width = 192.dp,
            height = 128.dp
        )
        MediaCardVariant.THUMBNAIL_STANDARD -> MediaCardDimensions(
            width = 192.dp,
            height = 108.dp
        )
        MediaCardVariant.THUMBNAIL_COMPACT_ROW -> MediaCardDimensions(
            width = 144.dp,
            height = 81.dp
        )
    }
}