package com.jycra.filmaico.core.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.jycra.filmaico.core.device.Platform
import com.jycra.filmaico.core.navigation.route.AppRoutes
import com.jycra.filmaico.domain.media.model.MediaType
import com.jycra.filmaico.feature.anime.detail.AnimeDetailRoute
import com.jycra.filmaico.feature.movie.detail.MovieDetailRoute
import com.jycra.filmaico.feature.serie.detail.SerieDetailRoute

fun NavGraphBuilder.detailRoute(
    platform: Platform,
    onNavigateToPlayer: (mediaType: MediaType, assetId: String) -> Unit,
    onNavigateBack: () -> Unit
) {
    composable(
        route = AppRoutes.DETAIL_WITH_ARGS,
        arguments = listOf(
            navArgument("mediaType") { type = NavType.StringType },
            navArgument("containerId") { type = NavType.StringType }
        )
    ) { backStackEntry ->

        val mediaType = backStackEntry.arguments?.getString("mediaType")

        when (mediaType) {
            MediaType.MOVIE.value -> MovieDetailRoute(
                platform = platform,
                onPlayAsset = onNavigateToPlayer,
                onNavigateBack = onNavigateBack
            )
            MediaType.SERIE.value -> SerieDetailRoute(
                platform = platform,
                onPlayAsset = onNavigateToPlayer,
                onNavigateBack = onNavigateBack
            )
            MediaType.ANIME.value -> AnimeDetailRoute(
                platform = platform,
                onPlayAsset = onNavigateToPlayer,
                onNavigateBack = onNavigateBack
            )
            else -> MovieDetailRoute(
                platform = platform,
                onPlayAsset = onNavigateToPlayer,
                onNavigateBack = onNavigateBack
            )
        }

    }
}

fun NavController.navigateToDetail(mediaType: MediaType, containerId: String) {
    navigate(AppRoutes.detailWithArgs(mediaType = mediaType.value, containerId = containerId))
}