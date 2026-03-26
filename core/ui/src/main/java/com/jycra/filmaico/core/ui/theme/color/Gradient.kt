package com.jycra.filmaico.core.ui.theme.color

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

object Gradient {

    @Composable
    fun verticalTopBarGradient(): Brush {
        return Brush.verticalGradient(
            colorStops = arrayOf(
                0.0f to MaterialTheme.colorScheme.surfaceContainerLowest,
                0.64f to MaterialTheme.colorScheme.surfaceContainerLowest,
                1.0f to MaterialTheme.colorScheme.surfaceContainerLowest.copy(0.80f)
            )
        )
    }

    @Composable
    fun verticalBottomBarGradient(): Brush {
        return Brush.verticalGradient(
            colorStops = arrayOf(
                0.0f to MaterialTheme.colorScheme.surfaceContainerLowest.copy(0.80f),
                0.48f to MaterialTheme.colorScheme.surfaceContainerLowest,
                1.0f to MaterialTheme.colorScheme.surfaceContainerLowest
            )
        )
    }

    @Composable
    fun horizontalSideGradient(): Brush {
        return Brush.horizontalGradient(
            colorStops = arrayOf(
                0.0f to MaterialTheme.colorScheme.surfaceContainerLowest,
                0.48f to MaterialTheme.colorScheme.surfaceContainerLowest,
                1.0f to MaterialTheme.colorScheme.surfaceContainerLowest.copy(0.80f)
            )
        )
    }

    @Composable
    fun verticalDetailGradient(): Brush {
        return Brush.verticalGradient(
            colorStops = arrayOf(
                0.0f to MaterialTheme.colorScheme.surfaceContainerLowest,
                0.72f to MaterialTheme.colorScheme.surfaceContainerLowest,
                1.0f to MaterialTheme.colorScheme.surfaceContainerLowest.copy(0.80f)
            )
        )
    }

    @Composable
    fun verticalBackgroundGradient(): Brush {
        return Brush.verticalGradient(
            colorStops = arrayOf(
                0.0f to MaterialTheme.colorScheme.surfaceContainerLowest.copy(0.32f),
                0.96f to MaterialTheme.colorScheme.surfaceContainerLowest,
                1.0f to MaterialTheme.colorScheme.surfaceContainerLowest
            )
        )
    }

    @Composable
    fun playerControlGradient(): Brush {
        return Brush.verticalGradient(
            colorStops = arrayOf(
                0.0f to Color.Black.copy(0.80f),
                0.24f to Color.Black.copy(0.32f),
                0.5f to Color.Black.copy(0f),
                0.76f to Color.Black.copy(0.32f),
                1.0f to Color.Black.copy(0.80f)
            )
        )
    }

}