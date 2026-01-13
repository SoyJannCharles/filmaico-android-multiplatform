package com.jycra.filmaico.feature.movie.detail.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.jycra.filmaico.core.navigation.Platform
import com.jycra.filmaico.core.ui.R
import com.jycra.filmaico.core.ui.component.FilmaicoLogo
import com.jycra.filmaico.core.ui.feature.detail.background.mobile.SurroundingBackgroundTopCenter
import com.jycra.filmaico.core.ui.feature.detail.background.tv.SurroundingBackgroundTopEnd
import com.jycra.filmaico.core.ui.feature.detail.navigation.DetailTopBar
import com.jycra.filmaico.core.ui.feature.detail.synopsis.SynopsisSection
import com.jycra.filmaico.core.ui.theme.color.Gradient
import com.jycra.filmaico.domain.movie.model.localizedSynopsis
import com.jycra.filmaico.feature.movie.detail.MovieDetailUiEvent
import com.jycra.filmaico.feature.movie.detail.MovieDetailUiState

@Composable
fun MovieDetailLayout(
    state: MovieDetailUiState.Success,
    platform: Platform,
    onEvent: (MovieDetailUiEvent) -> Unit
) {

    when (platform) {
        Platform.MOBILE -> {
            MobileLayout(
                state = state,
                onEvent = onEvent
            )
        }
        Platform.TV -> {
            TvLayout(
                state = state,
                onEvent = onEvent
            )
        }
    }

}

@Composable
private fun MobileLayout(
    state: MovieDetailUiState.Success,
    onEvent: (MovieDetailUiEvent) -> Unit
) {

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(brush = Gradient.verticalDetailGradient()),
        contentAlignment = Alignment.TopCenter
    ) {

        SurroundingBackgroundTopCenter(
            backgroundUrl = state.movie.coverUrl
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .padding(
                    start = 16.dp,
                    end = 16.dp,
                    bottom = 16.dp
                )
        ) {

            DetailTopBar(
                onBackPressed = { onEvent(MovieDetailUiEvent.OnBackPressed) }
            )

            MovieInfo(
                platform = Platform.MOBILE,
                movie = state.movie
            )

            SynopsisSection(
                synopsis = state.movie.localizedSynopsis
            )

            Button(
                modifier = Modifier
                    .fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                onClick = { onEvent(MovieDetailUiEvent.OnStartPlayback(movie = state.movie)) },
            ) {

                Text(
                    style = MaterialTheme.typography.titleSmall,
                    text = stringResource(R.string.moviedetail_button_play),
                )

            }

        }

    }

}

@Composable
private fun TvLayout(
    state: MovieDetailUiState.Success,
    onEvent: (MovieDetailUiEvent) -> Unit
) {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.surfaceContainerLowest),
        contentAlignment = Alignment.TopEnd
    ) {

        SurroundingBackgroundTopEnd(state.movie.coverUrl)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp)
        ) {

            FilmaicoLogo()

            MovieInfo(
                platform = Platform.TV,
                movie = state.movie
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                shape = RoundedCornerShape(8.dp),
                onClick = { onEvent(MovieDetailUiEvent.OnStartPlayback(movie = state.movie)) },
            ) {

                Text(
                    style = MaterialTheme.typography.titleSmall,
                    text = stringResource(R.string.moviedetail_button_play),
                )

            }

        }

    }

}