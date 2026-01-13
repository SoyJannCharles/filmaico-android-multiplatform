package com.jycra.filmaico.feature.signin

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.jycra.filmaico.core.navigation.Platform
import com.jycra.filmaico.core.navigation.route.AppRoutes

fun NavGraphBuilder.signInRoute(
    platform: Platform,
    onNavigateToSignUp: () -> Unit,
    onNavigateToPay: () -> Unit,
    onNavigateToMain: () -> Unit
) {
    composable(route = AppRoutes.SIGN_IN) {
        SignInRoute(
            platform = platform,
            onNavigateToSignUp = onNavigateToSignUp,
            onNavigateToPay = onNavigateToPay,
            onNavigateToHome = onNavigateToMain
        )
    }
}

fun NavController.navigateToSignIn() {
    navigate(route = AppRoutes.SIGN_IN) {
        popUpTo(route = AppRoutes.SPLASH) {
            inclusive = true
        }
    }
}

fun NavController.navigateToSignInAfterCancel() {
    navigate(route = AppRoutes.SIGN_IN) {
        popUpTo(route = AppRoutes.PAY) {
            inclusive = true
        }
    }
}