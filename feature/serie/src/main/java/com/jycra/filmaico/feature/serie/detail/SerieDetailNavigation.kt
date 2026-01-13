package com.jycra.filmaico.feature.serie.detail

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.jycra.filmaico.core.navigation.Platform
import com.jycra.filmaico.core.navigation.route.AppRoutes

fun NavGraphBuilder.serieDetailRoute(
    platform: Platform,
    onNavigateToPlayer: (String) -> Unit,
    onNavigateBack: () -> Unit
) {
    composable(AppRoutes.SERIE_DETAIL_WITH_ARGS) {
        SerieDetailRoute(
            platform = platform,
            onNavigateToPlayer = onNavigateToPlayer,
            onNavigateBack = onNavigateBack
        )
    }
}

fun NavController.navigateToSerieDetail(
    serieId: String
) {
    navigate(AppRoutes.serieDetailWithArgs(serieId))
}