package com.jycra.filmaico.feature.signup

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.jycra.filmaico.core.navigation.Platform
import com.jycra.filmaico.core.navigation.route.AppRoutes

fun NavGraphBuilder.signUpRoute(
    platform: Platform,
    onNavigateToPay: () -> Unit,
    onNavigateToSignIn: () -> Unit
) {
    composable(route = AppRoutes.SIGN_UP) {
        SignUpRoute(
            platform = platform,
            onNavigateToPay = onNavigateToPay,
            onNavigateToSignIn = onNavigateToSignIn
        )
    }
}

fun NavController.navigateToSignUp() {
    navigate(route = AppRoutes.SIGN_UP)
}