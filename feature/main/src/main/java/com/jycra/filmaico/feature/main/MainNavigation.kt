package com.jycra.filmaico.feature.main

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.jycra.filmaico.core.device.Platform
import com.jycra.filmaico.core.navigation.route.AppRoutes
import com.jycra.filmaico.domain.media.model.MediaType

fun NavGraphBuilder.mainRoute(
    platform: Platform,
    onNavigateToDetail: (mediaType: MediaType, contentId: String) -> Unit,
    onNavigateToPlayer: (mediaType: MediaType, assetId: String) -> Unit,
    onNavigateToAuth: () -> Unit
) {
    composable(route = AppRoutes.MAIN) {
        MainRoute(
            platform = platform,
            onNavigateToPlayer = onNavigateToPlayer,
            onNavigateToDetail = onNavigateToDetail,
            onNavigateToAuth = onNavigateToAuth
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

fun NavController.navigateToMainFromAuth() {
    navigate(AppRoutes.MAIN) {
        popUpTo(AppRoutes.SIGN_IN) {
            inclusive = true
        }
    }
}

fun NavController.navigateToMainFromSubscription() {
    navigate(AppRoutes.MAIN) {
        popUpTo(AppRoutes.SUBSCRIPTION) {
            inclusive = true
        }
    }
}