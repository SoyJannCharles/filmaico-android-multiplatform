package com.jycra.filmaico.feature.pay

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.jycra.filmaico.core.navigation.Platform
import com.jycra.filmaico.core.navigation.route.AppRoutes

fun NavGraphBuilder.payRoute(
    platform: Platform,
    onNavigateToSignInAfterCancel: () -> Unit,
    onNavigateToHome: () -> Unit
) {
    composable(route = AppRoutes.PAY) {
        PayRoute(
            platform = platform,
            onNavigateToSignInAfterCancel = onNavigateToSignInAfterCancel,
            onNavigateToHome = onNavigateToHome
        )
    }
}

fun NavController.navigateToPayFromSplash() {
    navigate(AppRoutes.PAY) {
        popUpTo(AppRoutes.SPLASH) {
            inclusive = true
        }
    }
}

fun NavController.navigateToPayFromSignIn() {
    navigate(AppRoutes.PAY) {
        popUpTo(AppRoutes.SIGN_IN) {
            inclusive = true
        }
    }
}

fun NavController.navigateToPayFromSignUp() {
    navigate(AppRoutes.PAY) {
        popUpTo(AppRoutes.SIGN_IN) {
            inclusive = true
        }
    }
}