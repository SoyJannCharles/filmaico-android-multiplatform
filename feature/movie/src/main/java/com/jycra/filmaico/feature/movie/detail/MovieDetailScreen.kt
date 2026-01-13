package com.jycra.filmaico.feature.movie.detail

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.jycra.filmaico.core.navigation.Platform
import com.jycra.filmaico.feature.movie.detail.component.MovieDetailLayout

@Composable
fun MovieDetailScreen(
    uiState: MovieDetailUiState,
    platform: Platform,
    onEvent: (MovieDetailUiEvent) -> Unit
) {

    when (uiState) {
        is MovieDetailUiState.Loading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
        is MovieDetailUiState.Success -> {
            MovieDetailLayout(
                state = uiState,
                platform = platform,
                onEvent = onEvent
            )
        }
        is MovieDetailUiState.Error -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = uiState.message)
            }
        }
    }

}