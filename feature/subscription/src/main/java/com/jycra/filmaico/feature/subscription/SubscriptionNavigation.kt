package com.jycra.filmaico.feature.subscription

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.jycra.filmaico.core.device.Platform
import com.jycra.filmaico.core.navigation.route.AppRoutes

fun NavGraphBuilder.subscriptionRoute(
    platform: Platform,
    onNavigateToAuth: () -> Unit,
    onNavigateToMain: () -> Unit
) {
    composable(route = AppRoutes.SUBSCRIPTION) {
        SubscriptionRoute(
            platform = platform,
            onNavigateToAuth = onNavigateToAuth,
            onNavigateToMain = onNavigateToMain
        )
    }
}

fun NavController.navigateToSubscriptionFromSplash() {
    navigate(AppRoutes.SUBSCRIPTION) {
        popUpTo(AppRoutes.SPLASH) {
            inclusive = true
        }
    }
}

fun NavController.navigateToSubscriptionFromAuth() {
    navigate(AppRoutes.SUBSCRIPTION) {
        popUpTo(AppRoutes.SIGN_IN) {
            inclusive = true
        }
    }
}

fun NavController.navigateToSubscriptionFromSignUp() {
    navigate(AppRoutes.SUBSCRIPTION) {
        popUpTo(AppRoutes.SIGN_IN) {
            inclusive = true
        }
    }
}