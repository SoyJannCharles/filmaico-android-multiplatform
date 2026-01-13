package com.jycra.filmaico.feature.splash

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.jycra.filmaico.core.navigation.Platform
import com.jycra.filmaico.core.ui.component.dialog.NoInternetDialog
import com.jycra.filmaico.feature.splash.component.UpdateRequiredScreen

@Composable
fun SplashScreen(
    uiState: SplashUiState,
    platform: Platform
) {

    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {

        when (uiState) {
            is SplashUiState.Loading, is SplashUiState.NoNetwork -> {
                when (platform) {
                    Platform.MOBILE -> SplashScreenMobile()
                    Platform.TV -> SplashScreenTv()
                }
            }
            is SplashUiState.UpdateRequired -> {
                UpdateRequiredScreen(
                    oldVersion = uiState.oldVersion,
                    newVersion = uiState.newVersion
                )
            }
        }

        if (uiState is SplashUiState.NoNetwork) {
            NoInternetDialog()
        }

    }

}

@Composable
private fun SplashScreenMobile() {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surfaceContainerLowest),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }

}

@Composable
private fun SplashScreenTv() {

    SplashScreenMobile()

}