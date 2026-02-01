package com.jycra.filmaico.feature.splash

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.jycra.filmaico.feature.splash.component.UpdateRequiredScreen

@Composable
fun SplashScreen(
    uiState: SplashUiState
) {

    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {

        when (uiState) {
            is SplashUiState.Cheking-> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.surfaceContainerLowest),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            is SplashUiState.UpdateRequired -> {
                UpdateRequiredScreen(
                    currentVersion = uiState.currentVersion,
                    serverVersion = uiState.serverVersion
                )
            }
            is SplashUiState.Error -> {

            }
        }

    }

}