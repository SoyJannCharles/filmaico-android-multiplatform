package com.jycra.filmaico.feature.movie.detail

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jycra.filmaico.core.device.Platform
import com.jycra.filmaico.domain.media.model.MediaType

@Composable
fun MovieDetailRoute(
    viewModel: MovieDetailViewModel = hiltViewModel(),
    platform: Platform,
    onPlayAsset: (mediaType: MediaType, assetId: String) -> Unit,
    onNavigateBack: () -> Unit
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is MovieDetailUiEffect.NavigateToPlayer -> onPlayAsset(MediaType.MOVIE, effect.mediaId)
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