package com.jycra.filmaico.tv

import android.os.Bundle
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
import androidx.tv.material3.ExperimentalTvMaterial3Api
import com.jycra.filmaico.core.app.AppViewModel
import com.jycra.filmaico.core.navigation.route.AppRoutes
import com.jycra.filmaico.core.ui.component.banner.NetworkStatusBanner
import com.jycra.filmaico.core.ui.theme.FilmaicoTheme
import com.jycra.filmaico.domain.network.ConnectivityObserver
import com.jycra.filmaico.domain.user.util.SessionStatus
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TvMainActivity : ComponentActivity() {

    private val mainViewModel: AppViewModel by viewModels()

    @OptIn(ExperimentalTvMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {

            val navController = rememberNavController()

            val sessionStatus by mainViewModel.sessionStatus.collectAsStateWithLifecycle()
            val isSubActive by mainViewModel.isSubscriptionActive.collectAsStateWithLifecycle()

            val networkStatus by mainViewModel.networkStatus.collectAsStateWithLifecycle()

            LaunchedEffect(sessionStatus, isSubActive) {
                val currentRoute = navController.currentBackStackEntry?.destination?.route
                val status = sessionStatus

                when (status) {

                    is SessionStatus.Authenticated -> {

                        val destination = if (isSubActive) AppRoutes.MAIN else AppRoutes.SUBSCRIPTION

                        if (currentRoute == AppRoutes.SIGN_IN || currentRoute == AppRoutes.SPLASH) {
                            navController.navigate(destination) { popUpTo(0) { inclusive = true } }
                        } else if (!isSubActive && currentRoute != AppRoutes.SUBSCRIPTION) {
                            navController.navigate(AppRoutes.SUBSCRIPTION) {
                                popUpTo(AppRoutes.MAIN) { inclusive = true }
                            }
                        }

                    }

                    is SessionStatus.Unauthenticated, SessionStatus.MissedDocument -> {
                        if (currentRoute != AppRoutes.SIGN_IN && currentRoute != AppRoutes.SPLASH) {
                            navController.navigate(AppRoutes.SIGN_IN) {
                                popUpTo(0) { inclusive = true }
                            }
                        }
                    }

                    is SessionStatus.Checking -> {
                    }

                }

            }

            FilmaicoTheme(
                platform = "tv",
                darkTheme = true
            ) {

                Column(modifier = Modifier.fillMaxSize()) {

                    Surface(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth(),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        AppTvNavHost(navController)
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