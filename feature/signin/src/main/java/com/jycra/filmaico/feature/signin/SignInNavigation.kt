package com.jycra.filmaico.feature.signin

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.jycra.filmaico.core.navigation.route.AppRoutes

fun NavGraphBuilder.signInRoute(
    onNavigateToSignUp: () -> Unit,
    onNavigateToSubscription: () -> Unit,
    onNavigateToMain: () -> Unit
) {
    composable(route = AppRoutes.SIGN_IN) {
        SignInRoute(
            onNavigateToSignUp = onNavigateToSignUp,
            onNavigateToSubscription = onNavigateToSubscription,
            onNavigateToMain = onNavigateToMain
        )
    }
}

fun NavController.navigateToAuth() {
    navigate(route = AppRoutes.SIGN_IN) {
        popUpTo(route = AppRoutes.SPLASH) {
            inclusive = true
        }
    }
}

fun NavController.navigateToAuthAfterCancel() {
    navigate(route = AppRoutes.SIGN_IN) {
        popUpTo(route = AppRoutes.SUBSCRIPTION) {
            inclusive = true
        }
    }
}

fun NavController.navigateToAuthAfterSignOut() {
    navigate(route = AppRoutes.SIGN_IN) {
        popUpTo(route = AppRoutes.MAIN) {
            inclusive = true
        }
    }
}