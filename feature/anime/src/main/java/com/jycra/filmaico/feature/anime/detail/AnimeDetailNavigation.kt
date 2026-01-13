package com.jycra.filmaico.feature.anime.detail

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.jycra.filmaico.core.navigation.Platform
import com.jycra.filmaico.core.navigation.route.AppRoutes

fun NavGraphBuilder.animeDetailRoute(
    platform: Platform,
    onNavigateToPlayer: (String) -> Unit,
    onNavigateBack: () -> Unit
) {
    composable(AppRoutes.ANIME_DETAIL_WITH_ARGS) {
        AnimeDetailRoute(
            platform = platform,
            onNavigateToPlayer = onNavigateToPlayer,
            onNavigateBack = onNavigateBack
        )
    }
}

fun NavController.navigateToAnimeDetail(
    animeId: String
) {
    navigate(AppRoutes.animeDetailWithArgs(animeId))
}