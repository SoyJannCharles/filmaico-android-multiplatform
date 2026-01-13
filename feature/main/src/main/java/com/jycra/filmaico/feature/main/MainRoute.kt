package com.jycra.filmaico.feature.main

import androidx.compose.runtime.Composable
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.jycra.filmaico.core.navigation.Platform

@Composable
fun MainRoute(
    viewModel: MainViewModel = hiltViewModel(),
    platform: Platform,
    onNavigateToDetail: (contentType: String, contentId: String) -> Unit,
    onNavigateToPlayer: (contentType: String, contentId: String) -> Unit,
    onNavigateToProfile: () -> Unit
) {

    when (platform) {
        Platform.MOBILE -> MainScaffoldMobile(
            platform = platform,
            onNavigateToDetail = onNavigateToDetail,
            onNavigateToPlayer = onNavigateToPlayer,
            onNavigateToProfile = onNavigateToProfile
        )
        Platform.TV -> MainScaffoldTv(
            platform = platform,
            onNavigateToDetail = onNavigateToDetail,
            onNavigateToPlayer = onNavigateToPlayer
        )
    }

}