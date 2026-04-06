package com.jycra.filmaico.feature.movie.detail

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jycra.filmaico.core.device.Platform
import com.jycra.filmaico.core.ui.feature.detail.internal.SurroundingBackgroundTopCenter
import com.jycra.filmaico.core.ui.feature.detail.internal.SurroundingBackgroundTopEnd
import com.jycra.filmaico.core.ui.theme.color.Gradient
import com.jycra.filmaico.domain.media.model.Media
import com.jycra.filmaico.domain.media.util.extesion.localizedImageUrl
import com.jycra.filmaico.domain.stream.util.StreamExtractionState
import com.jycra.filmaico.feature.movie.detail.component.MovieDetailContent

@Composable
fun MovieDetailScreen(
    uiState: MovieDetailUiState,
    platform: Platform,
    extractionState: StreamExtractionState,
    onEvent: (MovieDetailUiEvent) -> Unit
) {

    when (uiState) {
        is MovieDetailUiState.Loading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
        is MovieDetailUiState.Success -> {
            Screen(
                platform = platform,
                extractionState = extractionState,
                media = uiState.media,
                onStartPlayback = {
                    onEvent(MovieDetailUiEvent.PlayAsset)
                },
                onBackPressed = {
                    onEvent(MovieDetailUiEvent.OnBackPressed)
                }
            )
        }
        is MovieDetailUiState.Error -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = uiState.message)
            }
        }
    }

}

@Composable
fun Screen(
    platform: Platform,
    extractionState: StreamExtractionState,
    media: Media.Container,
    onStartPlayback: () -> Unit,
    onBackPressed: () -> Unit
) {

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .then(
                when (platform) {
                    Platform.MOBILE ->
                        Modifier.background(brush = Gradient.verticalDetailGradient())
                    Platform.TV ->
                        Modifier.background(color = MaterialTheme.colorScheme.surfaceContainerLowest)
                }
            ),
        contentAlignment = when (platform) {
            Platform.MOBILE -> Alignment.TopCenter
            Platform.TV -> Alignment.TopEnd
        }
    ) {

        when (platform) {
            Platform.MOBILE ->
                SurroundingBackgroundTopCenter(backgroundUrl = media.localizedImageUrl)
            Platform.TV ->
                SurroundingBackgroundTopEnd(backgroundUrl = media.localizedImageUrl)
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .then(
                    when (platform) {
                        Platform.MOBILE -> {
                            Modifier
                                .statusBarsPadding()
                                .padding(
                                    start = 16.dp,
                                    end = 16.dp,
                                    bottom = 16.dp
                                )
                        }
                        Platform.TV -> Modifier.padding(32.dp)
                    }
                )
        ) {

            MovieDetailContent(
                platform = platform,
                extractionState = extractionState,
                media = media,
                onStartPlayback = onStartPlayback,
                onBackPressed = onBackPressed
            )

        }

    }

}