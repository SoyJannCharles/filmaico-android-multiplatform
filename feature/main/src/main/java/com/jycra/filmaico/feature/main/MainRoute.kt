package com.jycra.filmaico.feature.main

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jycra.filmaico.core.device.Platform
import com.jycra.filmaico.domain.media.model.MediaType

@Composable
fun MainRoute(
    viewModel: MainViewModel = hiltViewModel(),
    platform: Platform,
    onNavigateToDetail: (mediaType: MediaType, containerId: String) -> Unit,
    onNavigateToPlayer: (mediaType: MediaType, assetId: String) -> Unit,
    onNavigateToAuth: () -> Unit
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    MainScaffold(
        uiState = uiState,
        platform = platform,
        onNavigateToDetail = onNavigateToDetail,
        onNavigateToPlayer = onNavigateToPlayer,
        onNavigateToAuth = onNavigateToAuth
    )

}