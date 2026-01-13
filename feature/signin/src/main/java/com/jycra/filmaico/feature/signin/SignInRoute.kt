package com.jycra.filmaico.feature.signin

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jycra.filmaico.core.navigation.Platform

@Composable
fun SignInRoute(
    viewModel: SignInViewModel = hiltViewModel(),
    platform: Platform,
    onNavigateToSignUp: () -> Unit,
    onNavigateToPay: () -> Unit,
    onNavigateToHome: () -> Unit
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                SignInUiEffect.NavigateToSignUp -> onNavigateToSignUp()
                SignInUiEffect.NavigateToPay -> onNavigateToPay()
                SignInUiEffect.NavigateToHome -> onNavigateToHome()
            }
        }
    }

    SignInScreen(
        uiState = uiState,
        platform = platform,
        onEvent = viewModel::onEvent
    )

}