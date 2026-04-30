package com.jycra.filmaico.feature.update

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.jycra.filmaico.core.navigation.route.AppRoutes

fun NavGraphBuilder.updateRoute() {

    composable(route = AppRoutes.UPDATE) {
        UpdateRoute()
    }

}