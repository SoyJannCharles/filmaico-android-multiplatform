package com.jycra.filmaico.feature.home

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jycra.filmaico.core.navigation.Platform

@Composable
fun HomeRoute(
    viewModel: HomeViewModel = hiltViewModel(),
    platform: Platform,
    mainScaffoldPadding: PaddingValues,
    onNavigateToDetail: (contentType: String, contentId: String) -> Unit,
    onNavigateToPlayer: (contentType: String, contentId: String) -> Unit,
    onNavigateToProfile: () -> Unit
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is HomeUiEffect.NavigateToDetail -> {
                    onNavigateToDetail(effect.contentType, effect.contentId)
                }
                is HomeUiEffect.NavigateToPlayer -> {
                    onNavigateToPlayer(effect.contentType, effect.contentId)
                }
                is HomeUiEffect.NavigateToProfile -> onNavigateToProfile()
            }
        }
    }

    HomeScaffoldMobile(
        uiState = uiState,
        platform = platform,
        mainScaffoldPadding = mainScaffoldPadding,
        onEvent = viewModel::onEvent // Pasamos la función onEvent para que los hijos la usen
    )

}