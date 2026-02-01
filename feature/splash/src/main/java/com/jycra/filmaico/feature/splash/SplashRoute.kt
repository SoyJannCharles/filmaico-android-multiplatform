package com.jycra.filmaico.feature.splash

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun SplashRoute(
    viewModel: SplashViewModel = hiltViewModel(),
    onNavigateToAuth: () -> Unit,
    onNavigateToSubscription: () -> Unit,
    onNavigateToMain: () -> Unit
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is SplashUiEffect.NavigateToAuth ->
                    onNavigateToAuth()
                is SplashUiEffect.NavigateToSubscription ->
                    onNavigateToSubscription()
                is SplashUiEffect.NavigateToMain ->
                    onNavigateToMain()
            }
        }
    }

    SplashScreen(
        uiState = uiState
    )

}