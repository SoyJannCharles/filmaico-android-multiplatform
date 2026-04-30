package com.jycra.filmaico.tv

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.jycra.filmaico.core.device.Platform
import com.jycra.filmaico.core.navigation.detailRoute
import com.jycra.filmaico.core.navigation.navigateToDetail
import com.jycra.filmaico.core.navigation.route.AppRoutes
import com.jycra.filmaico.core.ui.component.dialog.ErrorDialog
import com.jycra.filmaico.feature.main.mainRoute
import com.jycra.filmaico.feature.main.navigateToMainFromAuth
import com.jycra.filmaico.feature.player.navigateToPlayer
import com.jycra.filmaico.feature.player.playerRoute
import com.jycra.filmaico.feature.signin.navigateToAuthAfterCancel
import com.jycra.filmaico.feature.signin.navigateToAuthAfterSignOut
import com.jycra.filmaico.feature.signin.signInRoute
import com.jycra.filmaico.feature.signup.navigateToSignUp
import com.jycra.filmaico.feature.signup.signUpRoute
import com.jycra.filmaico.feature.splash.splashRoute
import com.jycra.filmaico.feature.subscription.navigateToSubscriptionFromAuth
import com.jycra.filmaico.feature.subscription.navigateToSubscriptionFromSignUp
import com.jycra.filmaico.feature.subscription.subscriptionRoute
import com.jycra.filmaico.feature.update.updateRoute

@Composable
fun AppTvNavHost(
    navController: NavHostController = rememberNavController()
) {

    var playbackError by remember { mutableStateOf<String?>(null) }

    if (playbackError != null) {
        ErrorDialog(
            title = "Error de Conexión",
            message = playbackError!!,
            onDismiss = { playbackError = null }
        )
    }

    NavHost(navController = navController, startDestination = AppRoutes.SPLASH) {

        splashRoute()

        updateRoute()

        signInRoute(
            platform = Platform.TV,
            onNavigateToSignUp = {
                navController.navigateToSignUp()
            },
            onNavigateToSubscription = {
                navController.navigateToSubscriptionFromAuth()
            },
            onNavigateToMain = {
                navController.navigateToMainFromAuth()
            }
        )

        signUpRoute(
            onNavigateToAuth = {
                navController.popBackStack()
            },
            onNavigateToSubscription = {
                navController.navigateToSubscriptionFromSignUp()
            }
        )

        subscriptionRoute(
            platform = Platform.TV,
            onNavigateToAuth = {
                navController.navigateToAuthAfterCancel()
            }
        )

        mainRoute(
            platform = Platform.TV,
            onNavigateToPlayer = { mediaType, assetId ->
                navController.navigateToPlayer(mediaType, assetId)
            },
            onNavigateToDetail = { mediaType, containerId ->
                navController.navigateToDetail(mediaType, containerId)
            },
            onNavigateToAuth = {
                navController.navigateToAuthAfterSignOut()
            }
        )

        detailRoute(
            platform = Platform.TV,
            onNavigateToPlayer = { mediaType, assetId ->
                navController.navigateToPlayer(mediaType, assetId)
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