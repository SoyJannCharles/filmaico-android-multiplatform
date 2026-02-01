package com.jycra.filmaico.core.ui.feature.detail.internal

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import com.jycra.filmaico.core.ui.component.image.BlurredImage

@Composable
fun SurroundingBackgroundTopEnd(
    backgroundUrl: String
) {

    BlurredImage(
        imageUrl = backgroundUrl,
        modifier = Modifier
            .fillMaxSize(0.72f)
    )

    Box(
        modifier = Modifier
            .fillMaxSize(0.72f)
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color.Transparent,
                        MaterialTheme.colorScheme.surfaceContainerLowest
                    )
                )
            )
    )

    Box(
        modifier = Modifier
            .fillMaxSize(0.72f)
            .background(
                Brush.horizontalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.surfaceContainerLowest,
                        Color.Transparent
                    )
                )
            )
    )

}