package com.jycra.filmaico.feature.player

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.jycra.filmaico.core.device.Platform
import com.jycra.filmaico.core.navigation.route.AppRoutes
import com.jycra.filmaico.domain.media.model.MediaType

fun NavGraphBuilder.playerRoute(
    platform: Platform,
    onNavigateBack: (error: String?) -> Unit
) {
    composable(
        route = AppRoutes.PLAYER_WITH_ARGS,
        arguments = listOf(
            navArgument("mediaType") { type = NavType.StringType },
            navArgument("assetId") { type = NavType.StringType }
        )
    ) {
        PlayerRoute(
            platform = platform,
            onNavigateBack = onNavigateBack
        )
    }
}

fun NavController.navigateToPlayer(mediaType: MediaType, assetId: String) {
    this.navigate(AppRoutes.playerWithArgs(mediaType = mediaType.value, assetId = assetId,))
}