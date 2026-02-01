package com.jycra.filmaico.feature.signup

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun SignUpRoute(
    viewModel: SignUpViewModel = hiltViewModel(),
    onNavigateToSubscription: () -> Unit,
    onNavigateToAuth: () -> Unit
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is SignUpUiEffect.NavigateToSubscription -> onNavigateToSubscription()
                is SignUpUiEffect.NavigateToAuth -> onNavigateToAuth()
            }
        }
    }

    SignUpScreen(
        uiState = uiState,
        onEvent = viewModel::onEvent
    )

}