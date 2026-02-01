package com.jycra.filmaico.feature.subscription

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jycra.filmaico.core.device.Platform

@Composable
fun SubscriptionRoute(
    viewModel: SubscriptionViewModel = hiltViewModel(),
    platform: Platform,
    onNavigateToAuth: () -> Unit,
    onNavigateToMain: () -> Unit
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(key1 = Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                SubscriptionUiEffect.NavigateToAuth -> onNavigateToAuth()
                SubscriptionUiEffect.NavigateToMain -> onNavigateToMain()
            }
        }
    }

    SubscriptionScreen(
        uiState = uiState,
        platform = platform,
        onEvent = viewModel::onEvent
    )

}