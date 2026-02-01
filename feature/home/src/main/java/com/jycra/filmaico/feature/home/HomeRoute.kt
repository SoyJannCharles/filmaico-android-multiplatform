package com.jycra.filmaico.feature.home

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jycra.filmaico.core.device.Platform
import com.jycra.filmaico.domain.media.model.MediaType

@Composable
fun HomeRoute(
    viewModel: HomeViewModel = hiltViewModel(),
    platform: Platform,
    contentPadding: PaddingValues,
    onOpenDetail: (mediaType: MediaType, containerId: String) -> Unit,
    onPlayAsset: (mediaType: MediaType, assetId: String) -> Unit,
    onNavigateToProfile: () -> Unit = {}
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is HomeUiEffect.OpenDetail -> {
                    onOpenDetail(effect.mediaType, effect.containerId)
                }
                is HomeUiEffect.PlayAsset -> {
                    onPlayAsset(effect.mediaType, effect.assetId)
                }
                is HomeUiEffect.NavigateToProfile -> onNavigateToProfile()
            }
        }
    }

    HomeScaffoldMobile(
        uiState = uiState,
        platform = platform,
        mainScaffoldPadding = contentPadding,
        onEvent = viewModel::onEvent
    )

}