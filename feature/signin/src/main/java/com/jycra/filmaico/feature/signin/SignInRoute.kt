package com.jycra.filmaico.feature.signin

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jycra.filmaico.core.device.Platform

@Composable
fun SignInRoute(
    viewModel: SignInViewModel = hiltViewModel(),
    platform: Platform,
    onNavigateToSignUp: () -> Unit,
    onNavigateToSubscription: () -> Unit,
    onNavigateToMain: () -> Unit
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        if (platform == Platform.TV)
            viewModel.createAuthSession()
    }

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                SignInUiEffect.NavigateToSignUp -> onNavigateToSignUp()
                SignInUiEffect.NavigateToSubscription -> onNavigateToSubscription()
                SignInUiEffect.NavigateToMain -> onNavigateToMain()
            }
        }
    }

    SignInScreen(
        uiState = uiState,
        platform = platform,
        onEvent = viewModel::onEvent
    )

}