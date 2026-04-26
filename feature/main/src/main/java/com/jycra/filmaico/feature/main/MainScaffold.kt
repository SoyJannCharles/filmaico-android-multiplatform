package com.jycra.filmaico.feature.main

import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.jycra.filmaico.core.device.Platform
import com.jycra.filmaico.core.navigation.route.MainRoutes
import com.jycra.filmaico.core.ui.component.navigation.bottom.BottomNav
import com.jycra.filmaico.core.ui.component.navigation.bottom.BottomNavItem
import com.jycra.filmaico.domain.media.model.MediaType
import com.jycra.filmaico.feature.anime.AnimeRoute
import com.jycra.filmaico.feature.channel.ChannelRoute
import com.jycra.filmaico.feature.home.HomeRoute
import com.jycra.filmaico.feature.main.common.MobileNavItems
import com.jycra.filmaico.feature.main.common.NavItems
import com.jycra.filmaico.feature.main.internal.Sidebar
import com.jycra.filmaico.feature.movie.MovieRoute
import com.jycra.filmaico.feature.panel.PanelRoute
import com.jycra.filmaico.feature.saves.SavesRoute
import com.jycra.filmaico.feature.search.SearchRoute
import com.jycra.filmaico.feature.serie.SerieRoute
import kotlinx.coroutines.android.awaitFrame

@Composable
fun MainScaffold(
    uiState: MainUiState,
    platform: Platform,
    onNavigateToDetail: (mediaType: MediaType, contentId: String) -> Unit,
    onNavigateToPlayer: (mediaType: MediaType, assetId: String) -> Unit,
    onNavigateToAuth: () -> Unit
) {

    when (uiState) {
        is MainUiState.Idle -> {}
        is MainUiState.Loading -> {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.surfaceContainerLowest),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
        is MainUiState.Ready -> {
            when (platform) {
                Platform.MOBILE -> MainScaffoldMobile(
                    platform = platform,
                    onNavigateToDetail = onNavigateToDetail,
                    onNavigateToPlayer = onNavigateToPlayer,
                    onNavigateToAuth = onNavigateToAuth
                )
                Platform.TV -> MainScaffoldTv(
                    platform = platform,
                    expirationText = uiState.expirationText.asString(),
                    onNavigateToDetail = onNavigateToDetail,
                    onNavigateToPlayer = onNavigateToPlayer,
                    onNavigateToAuth = onNavigateToAuth
                )
            }
        }
        is MainUiState.Error -> {
            Text(uiState.message)
        }
    }

}

@Composable
fun MainScaffoldMobile(
    platform: Platform,
    onNavigateToDetail: (mediaType: MediaType, contentId: String) -> Unit,
    onNavigateToPlayer: (mediaType: MediaType, assetId: String) -> Unit,
    onNavigateToAuth: () -> Unit
) {

    val navController = rememberNavController()
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

    val bottomNavItems = remember {
        MobileNavItems.entries.map {
            BottomNavItem(it.route, it.titleResId, it.iconResId)
        }
    }

    Scaffold(
        bottomBar = {
            BottomNav(
                currentRoute = currentRoute,
                items = bottomNavItems,
                onNavigate = { route ->
                    navController.navigate(route) {
                        launchSingleTop = true
                        restoreState = true
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                    }
                }
            )
        },
        contentWindowInsets = WindowInsets(0, 0, 0, 0)
    ) { innerPadding ->

        NavHost(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surfaceContainerLowest),
            navController = navController,
            startDestination = MainRoutes.HOME
        ) {

            composable(MainRoutes.HOME) {
                HomeRoute(
                    platform = platform,
                    contentPadding = innerPadding,
                    onOpenDetail = onNavigateToDetail,
                    onPlayAsset = onNavigateToPlayer,
                )
            }
            composable(MainRoutes.SEARCH) {
                SearchRoute(
                    platform = platform,
                    onPlayAsset = onNavigateToPlayer,
                    onOpenDetail = onNavigateToDetail
                )
            }
            composable(MainRoutes.SAVES) {
                SavesRoute(
                    platform = platform,
                    contentPadding = innerPadding,
                    onOpenDetail = onNavigateToDetail,
                    onPlayAsset = onNavigateToPlayer,
                )
            }
            composable(MainRoutes.PROFILE) {
                PanelRoute(
                    platform = platform,
                    contentPadding = innerPadding,
                    onSignOut = { onNavigateToAuth() }
                )
            }

        }

    }

}

@Composable
fun MainScaffoldTv(
    platform: Platform,
    expirationText: String,
    onNavigateToDetail: (mediaType: MediaType, containerId: String) -> Unit,
    onNavigateToPlayer: (mediaType: MediaType, assetId: String) -> Unit,
    onNavigateToAuth: () -> Unit
) {

    val navController = rememberNavController()
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

    val navItems = remember { NavItems.entries.toList() }

    val mainNavItems = listOf(
        NavItems.SEARCH,
        NavItems.HOME,
        NavItems.MY_LIST,
        NavItems.MORE
    )

    val additionalNavItems = listOf(
        NavItems.CHANNELS,
        NavItems.MOVIES,
        NavItems.SERIES,
        NavItems.ANIMES
    )

    val sidebarRequesters = remember {
        navItems.associate { it.route to FocusRequester() }
    }

    val contentRequesters = remember {
        navItems.associate { it.route to FocusRequester() }
    }

    LaunchedEffect(currentRoute) {

        currentRoute ?: return@LaunchedEffect

        val requester = contentRequesters[currentRoute]
        var focusHandled = false

        repeat(10) {

            if (!focusHandled) {

                awaitFrame()

                try {
                    requester?.requestFocus()
                    focusHandled = true
                } catch (e: Exception) {

                }

            }

        }

    }

    var isExpanded by remember { mutableStateOf(false) }

    val animatedNavWidth by animateDpAsState(
        targetValue = if (isExpanded) 220.dp else 72.dp,
        animationSpec = tween(durationMillis = 300, easing = LinearOutSlowInEasing),
        label = "NavWidth"
    )

    val contentPadding = PaddingValues(start = animatedNavWidth + 24.dp, end = 24.dp)

    Box(modifier = Modifier.fillMaxSize()) {

        NavHost(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surfaceContainerLowest),
            navController = navController,
            startDestination = MainRoutes.HOME
        ) {

            fun setupRoute(
                route: String,
                sidebarTarget: String = route,
                content: @Composable (PaddingValues, () -> Unit) -> Unit
            ) {
                composable(route) {
                    content(contentPadding) {
                        isExpanded = false
                        sidebarRequesters[sidebarTarget]?.requestFocus()
                    }
                }
            }

            setupRoute(MainRoutes.SEARCH) { contentPadding, onFocus ->
                SearchRoute(
                    platform = platform,
                    contentPadding = contentPadding,
                    onFocusLeft = onFocus,
                    contentFocusBeacon = contentRequesters[MainRoutes.SEARCH],
                    onOpenDetail = { mediaType, containerId -> onNavigateToDetail(mediaType, containerId) },
                    onPlayAsset = { mediaType, assetId -> onNavigateToPlayer(mediaType, assetId) }
                )
            }
            setupRoute(MainRoutes.HOME) { contentPadding, onFocus ->
                ChannelRoute(
                    platform = platform,
                    contentPadding = contentPadding,
                    contentFocusBeacon = contentRequesters[MainRoutes.HOME],
                    onFocusLeft = onFocus,
                    onPlayAsset = { assetId -> onNavigateToPlayer(MediaType.CHANNEL, assetId) }
                )
            }
            setupRoute(MainRoutes.SAVES) { contentPadding, onFocus ->
                SavesRoute(
                    platform = platform,
                    contentPadding = contentPadding,
                    contentFocusBeacon = contentRequesters[MainRoutes.SAVES],
                    onFocusLeft = onFocus,
                    onOpenDetail = { mediaType, containerId -> onNavigateToDetail(mediaType, containerId) },
                    onPlayAsset = { mediaType, assetId -> onNavigateToPlayer(mediaType, assetId) }
                )
            }
            setupRoute(MainRoutes.MOVIES, sidebarTarget = NavItems.MORE.route) { contentPadding, onFocus ->
                MovieRoute(
                    platform = platform,
                    contentPadding = contentPadding,
                    contentFocusBeacon = contentRequesters[MainRoutes.MOVIES],
                    onFocusLeft = onFocus,
                    onOpenDetail = { containerId -> onNavigateToDetail(MediaType.MOVIE, containerId) },
                    onPlayAsset = { mediaType, assetId -> onNavigateToPlayer(mediaType, assetId) }
                )
            }
            setupRoute(MainRoutes.SERIES, sidebarTarget = NavItems.MORE.route) { contentPadding, onFocus ->
                SerieRoute(
                    platform = platform,
                    contentPadding = contentPadding,
                    contentFocusBeacon = contentRequesters[MainRoutes.SERIES],
                    onFocusLeft = onFocus,
                    onOpenDetail = { containerId -> onNavigateToDetail(MediaType.SERIE, containerId) },
                    onPlayAsset = { mediaType, assetId -> onNavigateToPlayer(mediaType, assetId) }
                )
            }
            setupRoute(MainRoutes.CHANNELS, sidebarTarget = NavItems.MORE.route) { contentPadding, onFocus ->
                ChannelRoute(
                    platform = platform,
                    contentPadding = contentPadding,
                    contentFocusBeacon = contentRequesters[MainRoutes.CHANNELS],
                    onFocusLeft = onFocus,
                    onPlayAsset = { assetId -> onNavigateToPlayer(MediaType.CHANNEL, assetId) }
                )
            }
            setupRoute(MainRoutes.ANIME, sidebarTarget = NavItems.MORE.route) { contentPadding, onFocus ->
                AnimeRoute(
                    platform = platform,
                    contentPadding = contentPadding,
                    contentFocusBeacon = contentRequesters[MainRoutes.ANIME],
                    onFocusLeft = onFocus,
                    onOpenDetail = { containerId -> onNavigateToDetail(MediaType.ANIME, containerId) },
                    onPlayAsset = { mediaType, assetId -> onNavigateToPlayer(mediaType, assetId) }
                )
            }
            setupRoute(MainRoutes.PROFILE) { contentPadding, onFocus ->
                PanelRoute(
                    platform = platform,
                    contentPadding = contentPadding,
                    contentFocusBeacon = contentRequesters[MainRoutes.PROFILE],
                    onFocusLeft = onFocus,
                    onSignOut = { onNavigateToAuth() }
                )
            }

        }

        Sidebar(
            modifier = Modifier
                .align(Alignment.CenterStart),
            mainNavItems = mainNavItems,
            submenuNavItems = additionalNavItems,
            expirationText = expirationText,
            currentRoute = currentRoute,
            isExpanded = isExpanded,
            onExpandedChange = { isExpanded = it },
            onNavigate = { route ->
                if (currentRoute != route) {
                    isExpanded = false
                    navController.navigate(route) {
                        popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            },
            focusRequesters = sidebarRequesters,
            contentFocusBeacon = contentRequesters[currentRoute]
        )

    }

}