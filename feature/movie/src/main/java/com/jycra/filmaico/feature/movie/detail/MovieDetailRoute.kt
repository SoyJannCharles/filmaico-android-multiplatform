package com.jycra.filmaico.feature.movie.detail

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jycra.filmaico.core.navigation.Platform

@Composable
fun MovieDetailRoute(
    viewModel: MovieDetailViewModel = hiltViewModel(),
    platform: Platform,
    onNavigateToPlayer: (String) -> Unit,
    onNavigateBack: () -> Unit
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is MovieDetailUiEffect.NavigateToPlayer -> onNavigateToPlayer(effect.movieId)
                is MovieDetailUiEffect.NavigateBack -> onNavigateBack()
            }
        }
    }

    MovieDetailScreen(
        uiState = uiState,
        platform = platform,
        onEvent = viewModel::onEvent
    )

}