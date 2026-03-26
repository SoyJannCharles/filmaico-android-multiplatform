package com.jycra.filmaico.mobile

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
import com.jycra.filmaico.core.app.AppViewModel
import com.jycra.filmaico.core.navigation.route.AppRoutes
import com.jycra.filmaico.core.network.ConnectivityObserver
import com.jycra.filmaico.core.ui.component.banner.NetworkStatusBanner
import com.jycra.filmaico.core.ui.theme.FilmaicoTheme
import com.jycra.filmaico.data.user.util.SessionObserver
import dagger.hilt.android.AndroidEntryPoint
import kotlin.getValue

@AndroidEntryPoint
class MobileMainActivity : ComponentActivity() {

    private val mainViewModel: AppViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {

            val globalState by mainViewModel.globalState.collectAsStateWithLifecycle()
            val navController = rememberNavController()

            LaunchedEffect(globalState.sessionStatus) {

                val currentRoute = navController.currentBackStackEntry?.destination?.route

                when (globalState.sessionStatus) {
                    SessionObserver.SessionStatus.Invalid -> {
                        if (currentRoute != AppRoutes.SPLASH && currentRoute != AppRoutes.SIGN_IN) {
                            mainViewModel.signOut()
                            navController.navigate(AppRoutes.SPLASH) {
                                popUpTo(0) { inclusive = true }
                            }
                        }
                    }
                    SessionObserver.SessionStatus.Loading -> {

                    }
                    SessionObserver.SessionStatus.Valid -> {

                    }
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
                        visible = globalState.networkStatus != ConnectivityObserver.Status.Available,
                        enter = expandVertically(expandFrom = Alignment.Top) + fadeIn(),
                        exit = shrinkVertically(shrinkTowards = Alignment.Top) + fadeOut()
                    ) {
                        NetworkStatusBanner(status = globalState.networkStatus)
                    }

                }

            }

        }

    }

}