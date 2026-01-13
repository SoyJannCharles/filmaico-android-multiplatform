package com.jycra.filmaico.feature.movie.detail

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.jycra.filmaico.core.navigation.Platform
import com.jycra.filmaico.core.navigation.route.AppRoutes

fun NavGraphBuilder.movieDetailRoute(
    platform: Platform,
    onNavigateToPlayer: (String) -> Unit,
    onNavigateBack: () -> Unit
) {
    composable(AppRoutes.MOVIE_DETAIL_WITH_ARGS) {
        MovieDetailRoute(
            platform = platform,
            onNavigateToPlayer = onNavigateToPlayer,
            onNavigateBack = onNavigateBack
        )
    }
}

fun NavController.navigateToMovieDetail(
    movieId: String
) {
    navigate(AppRoutes.movieDetailWithArgs(movieId))
}