package com.jycra.filmaico.feature.player

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.jycra.filmaico.core.navigation.Platform
import com.jycra.filmaico.core.navigation.route.AppRoutes

fun NavGraphBuilder.playerRoute(
    platform: Platform,
    onNavigateBack: (error: String?) -> Unit
) {
    composable(
        route = AppRoutes.PLAYER_WITH_ARGS,
        arguments = listOf(
            navArgument("contentType") { type = NavType.StringType },
            navArgument("contentId") { type = NavType.StringType }
        )
    ) {
        PlayerRoute(
            platform = platform,
            onNavigateBack = onNavigateBack
        )
    }
}

fun NavController.navigateToPlayer(contentType: String, contentId: String) {
    this.navigate(AppRoutes.playerWithArgs(contentType, contentId))
}