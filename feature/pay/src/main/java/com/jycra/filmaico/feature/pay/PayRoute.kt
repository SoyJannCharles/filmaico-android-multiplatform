package com.jycra.filmaico.feature.pay

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jycra.filmaico.core.navigation.Platform

@Composable
fun PayRoute(
    viewModel: PayViewModel = hiltViewModel(),
    platform: Platform,
    onNavigateToSignInAfterCancel: () -> Unit,
    onNavigateToHome: () -> Unit
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(key1 = Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                PayUiEffect.NavigateToSignIn -> onNavigateToSignInAfterCancel()
                PayUiEffect.NavigateToHome -> onNavigateToHome()
            }
        }
    }

    PayScreen(
        uiState = uiState,
        platform = platform,
        onEvent = viewModel::onEvent
    )

}