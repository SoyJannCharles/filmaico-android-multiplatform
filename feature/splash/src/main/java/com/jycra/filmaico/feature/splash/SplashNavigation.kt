package com.jycra.filmaico.feature.splash

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.jycra.filmaico.core.navigation.route.AppRoutes

fun NavGraphBuilder.splashRoute(
    onNavigateToAuth: () -> Unit,
    onNavigateToSubscription: () -> Unit,
    onNavigateToMain: () -> Unit
) {

    composable(route = AppRoutes.SPLASH) {
        SplashRoute(
            onNavigateToAuth = onNavigateToAuth,
            onNavigateToSubscription = onNavigateToSubscription,
            onNavigateToMain = onNavigateToMain
        )
    }

}