package com.jycra.filmaico.mobile

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import com.jycra.filmaico.core.app.AppHealth
import com.jycra.filmaico.core.app.AppViewModel
import com.jycra.filmaico.core.navigation.route.AppRoutes
import com.jycra.filmaico.core.ui.component.banner.NetworkStatusBanner
import com.jycra.filmaico.core.ui.theme.FilmaicoTheme
import com.jycra.filmaico.domain.network.ConnectivityObserver
import com.jycra.filmaico.domain.user.util.SessionStatus
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MobileMainActivity : ComponentActivity() {

    private val mainViewModel: AppViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {

            val navController = rememberNavController()

            val appHealth by mainViewModel.appHealth.collectAsStateWithLifecycle()

            val sessionStatus by mainViewModel.sessionStatus.collectAsStateWithLifecycle()
            val isSubActive by mainViewModel.isSubscriptionActive.collectAsStateWithLifecycle()

            val networkStatus by mainViewModel.networkStatus.collectAsStateWithLifecycle()

            LaunchedEffect(appHealth, sessionStatus, isSubActive) {

                val currentRoute = navController.currentBackStackEntry?.destination?.route

                when (val health = appHealth) {

                    is AppHealth.UpdateRequired -> {
                        if (currentRoute != AppRoutes.UPDATE) {
                            navController.navigate(AppRoutes.UPDATE) {
                                popUpTo(0) { inclusive = true }
                            }
                        }
                        return@LaunchedEffect
                    }

                    is AppHealth.Error -> {}

                    is AppHealth.Ready -> {

                        Log.d("AppHealth", "SessionStatus: $sessionStatus")

                        when (val status = sessionStatus) {

                            is SessionStatus.Authenticated -> {

                                val destination =
                                    if (isSubActive) AppRoutes.MAIN else AppRoutes.SUBSCRIPTION

                                if (
                                    currentRoute == AppRoutes.SPLASH ||
                                    currentRoute == AppRoutes.UPDATE ||
                                    currentRoute == AppRoutes.SIGN_IN ||
                                    currentRoute == AppRoutes.SUBSCRIPTION
                                    ) {
                                    navController.navigate(destination) {
                                        popUpTo(0) {
                                            inclusive = true
                                        }
                                    }
                                } else if (!isSubActive) {
                                    navController.navigate(AppRoutes.SUBSCRIPTION) {
                                        popUpTo(AppRoutes.MAIN) { inclusive = true }
                                    }
                                }

                            }

                            is SessionStatus.Unauthenticated, SessionStatus.MissedDocument -> {
                                if (currentRoute != AppRoutes.SIGN_IN) {
                                    navController.navigate(AppRoutes.SIGN_IN) {
                                        popUpTo(AppRoutes.SPLASH) { inclusive = true }
                                    }
                                }
                            }

                            is SessionStatus.Checking -> {}

                        }

                    }

                    AppHealth.Checking -> {}

                }

            }

            FilmaicoTheme(
                platform = "mobile",
                darkTheme = true
            ) {

                Column(modifier = Modifier.fillMaxSize()) {

                    Surface(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth(),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        AppMobileNavHost(navController)
                    }

                    AnimatedVisibility(
                        visible = networkStatus != ConnectivityObserver.Status.Available,
                        enter = expandVertically(expandFrom = Alignment.Top) + fadeIn(),
                        exit = shrinkVertically(shrinkTowards = Alignment.Top) + fadeOut()
                    ) {
                        NetworkStatusBanner(status = networkStatus)
                    }

                }

            }

        }

    }

}