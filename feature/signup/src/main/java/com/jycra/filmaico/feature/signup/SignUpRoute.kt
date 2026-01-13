package com.jycra.filmaico.feature.signup

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jycra.filmaico.core.navigation.Platform

@Composable
fun SignUpRoute(
    viewModel: SignUpViewModel = hiltViewModel(),
    platform: Platform,
    onNavigateToPay: () -> Unit,
    onNavigateToSignIn: () -> Unit
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is SignUpUiEffect.NavigateToPay -> onNavigateToPay()
                is SignUpUiEffect.NavigateToSignIn -> onNavigateToSignIn()
            }
        }
    }

    SignUpScreen(
        uiState = uiState,
        platform = platform,
        onEvent = viewModel::onEvent
    )

}