package com.jycra.filmaico.core.ui.util.shimmer

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.jycra.filmaico.core.device.Platform
import com.jycra.filmaico.core.ui.feature.media.util.variant.MediaCardVariant
import com.jycra.filmaico.core.ui.feature.media.util.dimens.getMediaCardDimensions

@Composable
fun ShimmerMediaCard(
    platform: Platform,
    variant: MediaCardVariant
) {

    val dimensions = getMediaCardDimensions(variant)
    val brush = rememberShimmerBrush()

    Column(modifier = Modifier.width(dimensions.width)) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(dimensions.height)
                .clip(RoundedCornerShape(12.dp))
                .background(brush)
        )

        Spacer(modifier = Modifier.height(12.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth(0.7f)
                .height(16.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(brush)
        )

    }

}