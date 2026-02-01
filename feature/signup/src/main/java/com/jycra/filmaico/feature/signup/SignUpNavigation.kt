package com.jycra.filmaico.feature.signup

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.jycra.filmaico.core.navigation.route.AppRoutes

fun NavGraphBuilder.signUpRoute(
    onNavigateToSubscription: () -> Unit,
    onNavigateToAuth: () -> Unit
) {
    composable(route = AppRoutes.SIGN_UP) {
        SignUpRoute(
            onNavigateToSubscription = onNavigateToSubscription,
            onNavigateToAuth = onNavigateToAuth
        )
    }
}

fun NavController.navigateToSignUp() {
    navigate(route = AppRoutes.SIGN_UP)
}