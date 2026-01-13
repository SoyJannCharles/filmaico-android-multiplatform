package com.jycra.filmaico.mobile

import android.content.pm.ActivityInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.jycra.filmaico.core.navigation.ContentType
import com.jycra.filmaico.core.navigation.Platform
import com.jycra.filmaico.core.navigation.route.AppRoutes
import com.jycra.filmaico.core.ui.SystemUiController
import com.jycra.filmaico.core.ui.component.dialog.ActionableDialog
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
fun AppMobileNavHost() {

    val navController = rememberNavController()

    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

    if (currentRoute != AppRoutes.PLAYER_WITH_ARGS) {
        SystemUiController(
            orientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
        )
    }

    var playbackError by remember { mutableStateOf<String?>(null) }

    if (playbackError != null) {
        ActionableDialog(
            onDismissRequest = { playbackError = null },
            onConfirm = { playbackError = null },
            title = "Ups! Hubo un problema",
            text = playbackError!!
        )
    }

    NavHost(navController = navController, startDestination = AppRoutes.SPLASH) {

        splashRoute(
            platform = Platform.MOBILE,
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
            platform = Platform.MOBILE,
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
            platform = Platform.MOBILE,
            onNavigateToPay = {
                navController.navigateToPayFromSignUp()
            },
            onNavigateToSignIn = {
                navController.popBackStack()
            }
        )

        payRoute(
            platform = Platform.MOBILE,
            onNavigateToSignInAfterCancel = {
                navController.navigateToSignInAfterCancel()
            },
            onNavigateToHome = {
                navController.navigateToMainFromPay()
            }
        )

        mainRoute(
            platform = Platform.MOBILE,
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
            platform = Platform.MOBILE,
            onNavigateToPlayer = { contentId ->
                navController.navigateToPlayer(ContentType.MOVIE, contentId)
            },
            onNavigateBack = {
                navController.popBackStack()
            }
        )

        serieDetailRoute(
            platform = Platform.MOBILE,
            onNavigateToPlayer = { contentId ->
                navController.navigateToPlayer(ContentType.SERIE, contentId)
            },
            onNavigateBack = {
                navController.popBackStack()
            }
        )

        animeDetailRoute(
            platform = Platform.MOBILE,
            onNavigateToPlayer = { contentId ->
                navController.navigateToPlayer(ContentType.ANIME, contentId)
            },
            onNavigateBack = {
                navController.popBackStack()
            }
        )

        playerRoute(
            platform = Platform.MOBILE,
            onNavigateBack = { error ->
                if (!error.isNullOrBlank()) {
                    playbackError = error
                }
                navController.popBackStack()
            }
        )

    }

}