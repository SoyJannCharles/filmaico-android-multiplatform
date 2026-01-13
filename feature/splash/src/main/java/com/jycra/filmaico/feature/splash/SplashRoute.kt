package com.jycra.filmaico.feature.splash

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jycra.filmaico.core.navigation.Platform

@Composable
fun SplashRoute(
    viewModel: SplashViewModel = hiltViewModel(),
    platform: Platform,
    onNavigateToSignIn: () -> Unit,
    onNavigateToPay: () -> Unit,
    onNavigateToHome: () -> Unit
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is SplashUiEffect.NavigateToSignIn ->
                    onNavigateToSignIn()
                is SplashUiEffect.NavigateToPay ->
                    onNavigateToPay()
                is SplashUiEffect.NavigateToHome ->
                    onNavigateToHome()
            }
        }
    }

    SplashScreen(
        uiState = uiState,
        platform = platform
    )

}