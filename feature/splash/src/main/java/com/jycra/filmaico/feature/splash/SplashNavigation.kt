package com.jycra.filmaico.feature.splash

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.jycra.filmaico.core.navigation.route.AppRoutes

fun NavGraphBuilder.splashRoute() {

    composable(route = AppRoutes.SPLASH) {
        SplashRoute()
    }

}