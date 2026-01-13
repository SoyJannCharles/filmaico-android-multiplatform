package com.jycra.filmaico.tv

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.jycra.filmaico.core.navigation.ContentType
import com.jycra.filmaico.core.navigation.Platform
import com.jycra.filmaico.core.navigation.route.AppRoutes
import com.jycra.filmaico.core.ui.component.dialog.ErrorDialog
import com.jycra.filmaico.feature.anime.detail.animeDetailRoute
import com.jycra.filmaico.feature.anime.detail.navigateToAnimeDetail
import com.jycra.filmaico.feature.main.mainRoute
import com.jycra.filmaico.feature.main.navigateToMainFromPay
import com.jycra.filmaico.feature.main.navigateToMainFromSignIn
import com.jycra.filmaico.feature.main.navigateToMainFromSplash
import com.jycra.filmaico.feature.movie.detail.movieDetailRoute
import com.jycra.filmaico.feature.movie.detail.navigateToMovieDetail
import com.jycra.filmaico.feature.pay.navigateToPayFromSignIn
import com.jycra.filmaico.feature.pay.navigateToPayFromSignUp
import com.jycra.filmaico.feature.pay.navigateToPayFromSplash
import com.jycra.filmaico.feature.pay.payRoute
import com.jycra.filmaico.feature.player.navigateToPlayer
import com.jycra.filmaico.feature.player.playerRoute
import com.jycra.filmaico.feature.serie.detail.navigateToSerieDetail
import com.jycra.filmaico.feature.serie.detail.serieDetailRoute
import com.jycra.filmaico.feature.signin.navigateToSignIn
import com.jycra.filmaico.feature.signin.navigateToSignInAfterCancel
import com.jycra.filmaico.feature.signin.signInRoute
import com.jycra.filmaico.feature.signup.navigateToSignUp
import com.jycra.filmaico.feature.signup.signUpRoute
import com.jycra.filmaico.feature.splash.splashRoute

@Composable
fun AppTvNavHost() {

    val navController = rememberNavController()

    var playbackError by remember { mutableStateOf<String?>(null) }

    if (playbackError != null) {
        ErrorDialog(
            title = "Error de Conexión",
            message = playbackError!!,
            onDismiss = { playbackError = null }
        )
    }

    NavHost(navController = navController, startDestination = AppRoutes.SPLASH) {

        splashRoute(
            platform = Platform.TV,
            onNavigateToSignIn = {
                navController.navigateToSignIn()
            },
            onNavigateToPay = {
                navController.navigateToPayFromSplash()
            },
            onNavigateToMain = {
                navController.navigateToMainFromSplash()
            }
        )

        signInRoute(
            platform = Platform.TV,
            onNavigateToSignUp = {
                navController.navigateToSignUp()
            },
            onNavigateToPay = {
                navController.navigateToPayFromSignIn()
            },
            onNavigateToMain = {
                navController.navigateToMainFromSignIn()
            }
        )

        signUpRoute(
            platform = Platform.TV,
            onNavigateToSignIn = {
                navController.popBackStack()
            },
            onNavigateToPay = {
                navController.navigateToPayFromSignUp()
            }
        )

        payRoute(
            platform = Platform.TV,
            onNavigateToSignInAfterCancel = {
                navController.navigateToSignInAfterCancel()
            },
            onNavigateToHome = {
                navController.navigateToMainFromPay()
            }
        )

        mainRoute(
            platform = Platform.TV,
            onNavigateToPlayer = { contentType, contentId ->
                navController.navigateToPlayer(contentType, contentId)
            },
            onNavigateToDetail = { contentType, contentId ->
                when (contentType) {
                    ContentType.MOVIE -> navController.navigateToMovieDetail(contentId)
                    ContentType.SERIE -> navController.navigateToSerieDetail(contentId)
                    ContentType.ANIME -> navController.navigateToAnimeDetail(contentId)
                }
            },
            onNavigateToProfile = {

            }
        )

        movieDetailRoute(
            platform = Platform.TV,
            onNavigateToPlayer = { contentId ->
                navController.navigateToPlayer(ContentType.MOVIE, contentId)
            },
            onNavigateBack = {
                navController.popBackStack()
            }
        )

        serieDetailRoute(
            platform = Platform.TV,
            onNavigateToPlayer = { contentId ->
                navController.navigateToPlayer(ContentType.SERIE, contentId)
            },
            onNavigateBack = {
                navController.popBackStack()
            }
        )

        animeDetailRoute(
            platform = Platform.TV,
            onNavigateToPlayer = { contentId ->
                navController.navigateToPlayer(ContentType.ANIME, contentId)
            },
            onNavigateBack = {
                navController.popBackStack()
            }
        )

        playerRoute(
            platform = Platform.TV,
            onNavigateBack = { error ->
                if (!error.isNullOrBlank()) {
                    playbackError = error
                }
                navController.previousBackStackEntry
                    ?.savedStateHandle
                    ?.set("from_player", true)
                navController.popBackStack()
            }
        )

    }

}