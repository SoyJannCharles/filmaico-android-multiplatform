package com.jycra.filmaico.feature.splash

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jycra.filmaico.core.app.AppViewModel
import com.jycra.filmaico.domain.user.util.SessionStatus

@Composable
fun SplashRoute(
    appViewModel: AppViewModel = hiltViewModel(),
    viewModel: SplashViewModel = hiltViewModel(),
    onNavigateToAuth: () -> Unit,
    onNavigateToSubscription: () -> Unit,
    onNavigateToMain: () -> Unit
) {

    val sessionStatus by appViewModel.sessionStatus.collectAsStateWithLifecycle()
    val splashUiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(splashUiState, sessionStatus) {
        if (splashUiState is SplashUiState.ReadyToCheckSession) {
            when (val status = sessionStatus) {
                is SessionStatus.Authenticated -> {
                    status.user.subscription?.let {
                        if (it.isActive())
                            onNavigateToMain()
                        else
                            onNavigateToSubscription()
                    }
                }
                is SessionStatus.Unauthenticated, SessionStatus.MissedDocument -> {
                    onNavigateToAuth()
                }
                is SessionStatus.Checking -> {

                }
            }
        }
    }

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