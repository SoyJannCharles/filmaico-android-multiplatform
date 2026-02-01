package com.jycra.filmaico.core.ui.feature.detail.util.dimens

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.jycra.filmaico.core.device.Platform

data class MediaCoverDimensions(
    val width: Dp,
    val height: Dp
)

@Composable
fun getCoverDimensions(
    platform: Platform
): MediaCoverDimensions {
    return when (platform) {
        Platform.MOBILE -> {
            MediaCoverDimensions(
                width = 128.dp,
                height = 192.dp
            )
        }
        Platform.TV -> {
            MediaCoverDimensions(
                width = 160.dp,
                height = 224.dp
            )
        }
    }
}