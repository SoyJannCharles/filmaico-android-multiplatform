package com.jycra.filmaico.feature.splash

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.jycra.filmaico.core.navigation.Platform
import com.jycra.filmaico.core.navigation.route.AppRoutes

fun NavGraphBuilder.splashRoute(
    platform: Platform,
    onNavigateToSignIn: () -> Unit,
    onNavigateToPay: () -> Unit,
    onNavigateToMain: () -> Unit
) {
    composable(route = AppRoutes.SPLASH) {
        SplashRoute(
            platform = platform,
            onNavigateToSignIn = onNavigateToSignIn,
            onNavigateToPay = onNavigateToPay,
            onNavigateToHome = onNavigateToMain
        )
    }
}