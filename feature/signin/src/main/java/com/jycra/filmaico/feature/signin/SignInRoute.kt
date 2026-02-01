package com.jycra.filmaico.feature.signin

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun SignInRoute(
    viewModel: SignInViewModel = hiltViewModel(),
    onNavigateToSignUp: () -> Unit,
    onNavigateToSubscription: () -> Unit,
    onNavigateToMain: () -> Unit
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

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
        onEvent = viewModel::onEvent
    )

}