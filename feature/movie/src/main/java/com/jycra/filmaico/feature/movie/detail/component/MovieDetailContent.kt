package com.jycra.filmaico.feature.movie.detail.component

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.jycra.filmaico.core.device.Platform
import com.jycra.filmaico.core.ui.R
import com.jycra.filmaico.core.ui.component.FilmaicoLogo
import com.jycra.filmaico.core.ui.component.FilmaicoLogoSidebar
import com.jycra.filmaico.core.ui.feature.detail.internal.MediaCover
import com.jycra.filmaico.core.ui.feature.detail.internal.Synopsis
import com.jycra.filmaico.domain.media.model.Media
import com.jycra.filmaico.domain.media.util.extesion.localizedImageUrl
import com.jycra.filmaico.domain.media.util.extesion.localizedName
import com.jycra.filmaico.domain.media.util.extesion.localizedSynopsis
import com.jycra.filmaico.domain.stream.util.StreamExtractionState
import java.util.Calendar

@Composable
fun MovieDetailContent(
    platform: Platform,
    extractionState: StreamExtractionState,
    media: Media.Container,
    onStartPlayback: () -> Unit,
    onBackPressed: () -> Unit = {},
) {

    val isReady = extractionState is StreamExtractionState.Success

    val infiniteTransition = rememberInfiniteTransition(label = "GlowTransition")
    val glowProgress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "GlowProgress"
    )

    val buttonColor = MaterialTheme.colorScheme.primary

    when (platform) {
        Platform.MOBILE -> {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {

                IconButton(onClick = onBackPressed) {
                    Icon(
                        modifier = Modifier.size(24.dp),
                        tint = MaterialTheme.colorScheme.onSurface,
                        painter = painterResource(R.drawable.ic_arrow_back),
                        contentDescription = "Back"
                    )
                }

                FilmaicoLogo(modifier = Modifier.weight(1f))

                Spacer(modifier = Modifier.width(48.dp))

            }
        }
        Platform.TV -> {
            FilmaicoLogoSidebar()
        }
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        if (platform == Platform.MOBILE) {

            MediaCover(
                coverUrl = media.localizedImageUrl,
                platform = platform,
                contentDescription = media.localizedName
            )

            Spacer(modifier = Modifier.width(16.dp))

        }

        Column(
            modifier = if (platform == Platform.TV) {
                Modifier
                    .weight(1f)
                    .padding(end = 128.dp)
            } else Modifier
        ) {

            Text(
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface,
                text = media.localizedName
            )

            Spacer(modifier = Modifier.height(8.dp))

            val releaseYear = media.airDate?.let { millis ->
                val calendar = Calendar.getInstance().apply { timeInMillis = millis }
                calendar.get(Calendar.YEAR).toString()
            } ?: ""

            Text(
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
                text = releaseYear
            )

            if (platform == Platform.TV) {

                Spacer(modifier = Modifier.height(16.dp))

                Synopsis(
                    platform = platform,
                    synopsis = media.localizedSynopsis
                )

            }

        }

        if (platform == Platform.TV) {
            MediaCover(
                modifier = Modifier
                    .padding(end = 128.dp),
                platform = platform,
                coverUrl = media.localizedImageUrl,
                contentDescription = media.localizedName
            )
        }

    }

    if (platform == Platform.MOBILE) {
        Synopsis(
            platform = platform,
            synopsis = media.localizedSynopsis
        )
    }

    Box(
        modifier = Modifier
            .then(
                if (extractionState is StreamExtractionState.Success) {
                    Modifier.drawBehind {

                        val maxBlur = 20.dp.toPx()

                        val glowAlpha = 0.32f - (0.2f * glowProgress)

                        drawIntoCanvas { canvas ->

                            val paint = Paint()
                            val frameworkPaint = paint.asFrameworkPaint()

                            frameworkPaint.color = Color.Transparent.toArgb()

                            frameworkPaint.setShadowLayer(
                                maxBlur,
                                0f,
                                0f,
                                buttonColor.copy(alpha = glowAlpha).toArgb()
                            )

                            canvas.drawRoundRect(
                                0f,
                                0f,
                                size.width,
                                size.height,
                                8.dp.toPx(),
                                8.dp.toPx(),
                                paint
                            )

                        }
                    }
                } else Modifier
            )
    ) {

        Button(
            modifier = Modifier
                .then(
                    if (platform == Platform.MOBILE) {
                        Modifier.fillMaxWidth()
                    } else Modifier
                ),
            shape = RoundedCornerShape(8.dp),
            onClick = onStartPlayback,
            colors = ButtonDefaults.buttonColors(containerColor = buttonColor),
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp) // Quitamos elevación nativa
        ) {

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                Icon(
                    modifier = Modifier
                        .size(24.dp),
                    painter = painterResource(R.drawable.ic_player_play),
                    contentDescription = "Reproducir"
                )

                Text(
                    style = MaterialTheme.typography.titleMedium,
                    text = stringResource(R.string.moviedetail_button_play)
                )

            }

        }

    }

}