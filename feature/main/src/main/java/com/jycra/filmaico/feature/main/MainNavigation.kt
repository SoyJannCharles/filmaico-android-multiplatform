package com.jycra.filmaico.feature.main

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.jycra.filmaico.core.navigation.Platform
import com.jycra.filmaico.core.navigation.route.AppRoutes

fun NavGraphBuilder.mainRoute(
    platform: Platform,
    onNavigateToDetail: (contentType: String, contentId: String) -> Unit,
    onNavigateToPlayer: (contentType: String, contentId: String) -> Unit,
    onNavigateToProfile: () -> Unit
) {
    composable(route = AppRoutes.MAIN) {
        MainRoute(
            platform = platform,
            onNavigateToPlayer = onNavigateToPlayer,
            onNavigateToDetail = onNavigateToDetail,
            onNavigateToProfile = onNavigateToProfile
        )
    }
}

fun NavController.navigateToMainFromSplash() {
    navigate(AppRoutes.MAIN) {
        popUpTo(AppRoutes.SPLASH) {
            inclusive = true
        }
    }
}

fun NavController.navigateToMainFromSignIn() {
    navigate(AppRoutes.MAIN) {
        popUpTo(AppRoutes.SIGN_IN) {
            inclusive = true
        }
    }
}

fun NavController.navigateToMainFromPay() {
    navigate(AppRoutes.MAIN) {
        popUpTo(AppRoutes.PAY) {
            inclusive = true
        }
    }
}