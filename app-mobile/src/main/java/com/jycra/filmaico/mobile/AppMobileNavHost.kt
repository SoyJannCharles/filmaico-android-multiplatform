package com.jycra.filmaico.mobile

import android.content.pm.ActivityInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.jycra.filmaico.core.device.Platform
import com.jycra.filmaico.core.navigation.route.AppRoutes
import com.jycra.filmaico.core.ui.SystemUiController
import com.jycra.filmaico.core.ui.component.dialog.ActionableDialog
import com.jycra.filmaico.feature.main.mainRoute
import com.jycra.filmaico.feature.main.navigateToMainFromSubscription
import com.jycra.filmaico.feature.main.navigateToMainFromAuth
import com.jycra.filmaico.feature.main.navigateToMainFromSplash
import com.jycra.filmaico.core.navigation.detailRoute
import com.jycra.filmaico.core.navigation.navigateToDetail
import com.jycra.filmaico.feature.subscription.navigateToSubscriptionFromAuth
import com.jycra.filmaico.feature.subscription.navigateToSubscriptionFromSignUp
import com.jycra.filmaico.feature.subscription.navigateToSubscriptionFromSplash
import com.jycra.filmaico.feature.subscription.subscriptionRoute
import com.jycra.filmaico.feature.player.navigateToPlayer
import com.jycra.filmaico.feature.player.playerRoute
import com.jycra.filmaico.feature.signin.navigateToAuth
import com.jycra.filmaico.feature.signin.navigateToAuthAfterCancel
import com.jycra.filmaico.feature.signin.signInRoute
import com.jycra.filmaico.feature.signup.navigateToSignUp
import com.jycra.filmaico.feature.signup.signUpRoute
import com.jycra.filmaico.feature.splash.splashRoute

@Composable
fun AppMobileNavHost(
    navController: NavHostController = rememberNavController()
) {

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
            onNavigateToAuth = {
                navController.navigateToAuth()
            },
            onNavigateToSubscription = {
                navController.navigateToSubscriptionFromSplash()
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
            onNavigateToSubscription = {
                navController.navigateToSubscriptionFromAuth()
            },
            onNavigateToMain = {
                navController.navigateToMainFromAuth()
            }
        )

        signUpRoute(
            onNavigateToSubscription = {
                navController.navigateToSubscriptionFromSignUp()
            },
            onNavigateToAuth = {
                navController.popBackStack()
            }
        )

        subscriptionRoute(
            platform = Platform.MOBILE,
            onNavigateToAuth = {
                navController.navigateToAuthAfterCancel()
            },
            onNavigateToMain = {
                navController.navigateToMainFromSubscription()
            }
        )

        mainRoute(
            platform = Platform.MOBILE,
            onNavigateToPlayer = { mediaType, assetId ->
                navController.navigateToPlayer(mediaType, assetId)
            },
            onNavigateToDetail = { mediaType, containerId ->
                navController.navigateToDetail(mediaType, containerId)
            },
            onNavigateToAuth = {

            }
        )

        detailRoute(
            platform = Platform.MOBILE,
            onNavigateToPlayer = { mediaType, assetId ->
                navController.navigateToPlayer(mediaType, assetId)
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