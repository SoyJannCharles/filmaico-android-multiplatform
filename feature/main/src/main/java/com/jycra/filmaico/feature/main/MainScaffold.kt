package com.jycra.filmaico.feature.main

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
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
import com.jycra.filmaico.core.navigation.Platform
import com.jycra.filmaico.core.navigation.route.MainRoutes
import com.jycra.filmaico.core.ui.component.navigation.bottom.BottomNav
import com.jycra.filmaico.core.ui.component.navigation.bottom.BottomNavItem
import com.jycra.filmaico.feature.anime.AnimeRoute
import com.jycra.filmaico.feature.channel.ChannelRoute
import com.jycra.filmaico.feature.home.HomeRoute
import com.jycra.filmaico.feature.main.common.MobileNavItems
import com.jycra.filmaico.feature.main.common.TvNavItems
import com.jycra.filmaico.feature.main.component.TvSideNavigation
import com.jycra.filmaico.feature.movie.MovieRoute
import com.jycra.filmaico.feature.search.SearchRoute
import com.jycra.filmaico.feature.serie.SerieRoute
import kotlinx.coroutines.android.awaitFrame

@Composable
fun MainScaffoldMobile(
    platform: Platform,
    onNavigateToDetail: (contentType: String, contentId: String) -> Unit,
    onNavigateToPlayer: (contentType: String, contentId: String) -> Unit,
    onNavigateToProfile: () -> Unit
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
            navController = navController,
            startDestination = MainRoutes.HOME,
            modifier = Modifier.
                fillMaxSize()
        ) {

            composable(MainRoutes.HOME) {
                HomeRoute(
                    platform = platform,
                    mainScaffoldPadding = innerPadding,
                    onNavigateToDetail = onNavigateToDetail,
                    onNavigateToPlayer = onNavigateToPlayer,
                    onNavigateToProfile = onNavigateToProfile
                )
            }
            composable(MainRoutes.SEARCH) {
                SearchRoute(
                    platform = platform,
                    onNavigateToPlayer = onNavigateToPlayer,
                    onNavigateToDetail = onNavigateToDetail
                )
            }
            composable(MainRoutes.MY_LIST) {
                HomeRoute(
                    platform = platform,
                    mainScaffoldPadding = innerPadding,
                    onNavigateToDetail = onNavigateToDetail,
                    onNavigateToPlayer = onNavigateToPlayer,
                    onNavigateToProfile = onNavigateToProfile
                )
            }
            composable(MainRoutes.PROFILE) {
                HomeRoute(
                    platform = platform,
                    mainScaffoldPadding = innerPadding,
                    onNavigateToDetail = onNavigateToDetail,
                    onNavigateToPlayer = onNavigateToPlayer,
                    onNavigateToProfile = onNavigateToProfile
                )
            }

        }

    }

}

@Composable
fun MainScaffoldTv(
    platform: Platform,
    onNavigateToDetail: (String, String) -> Unit,
    onNavigateToPlayer: (String, String) -> Unit
) {

    val navController = rememberNavController()
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

    val navItems = remember { TvNavItems.entries.toList() }

    val sidebarRequesters = remember {
        navItems.associate { it.route to FocusRequester() }
    }

    val contentRequesters = remember {
        navItems.associate { it.route to FocusRequester() }
    }

    LaunchedEffect(currentRoute) {

        currentRoute ?: return@LaunchedEffect

        awaitFrame()

        val requester = contentRequesters[currentRoute]
        requester?.requestFocus()

    }

    var isExpanded by remember { mutableStateOf(false) }

    val animatedNavWidth by animateDpAsState(
        targetValue = if (isExpanded) 220.dp else 72.dp,
        label = "NavWidth"
    )

    val contentPadding = PaddingValues(start = animatedNavWidth + 24.dp, end = 24.dp)

    Box(modifier = Modifier.fillMaxSize()) {

        NavHost(
            navController = navController,
            startDestination = MainRoutes.HOME,
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surfaceContainerLowest)
        ) {

            fun setupRoute(route: String, content: @Composable (PaddingValues, () -> Unit) -> Unit) {
                composable(route) {
                    content(contentPadding) {
                        isExpanded = false
                        sidebarRequesters[currentRoute]?.requestFocus()
                    }
                }
            }

            setupRoute(MainRoutes.SEARCH) { contentPadding, onFocus ->
                SearchRoute(
                    platform = platform,
                    contentPadding = contentPadding,
                    onFocusLeft = onFocus,
                    contentFocusBeacon = contentRequesters[MainRoutes.SEARCH],
                    onNavigateToPlayer = onNavigateToPlayer,
                    onNavigateToDetail = onNavigateToDetail
                )
            }
            setupRoute(MainRoutes.HOME) { contentPadding, onFocus ->
                ChannelRoute(
                    platform = platform,
                    contentPadding = contentPadding,
                    contentFocusBeacon = contentRequesters[MainRoutes.HOME],
                    onFocusLeft = onFocus
                ) { id -> onNavigateToPlayer("channel", id) }
            }
            setupRoute(MainRoutes.MY_LIST) { contentPadding, onFocus ->
                ChannelRoute(
                    platform = platform,
                    contentPadding = contentPadding,
                    contentFocusBeacon = contentRequesters[MainRoutes.MY_LIST],
                    onFocusLeft = onFocus
                ) { id -> onNavigateToPlayer("channel", id) }
            }
            setupRoute(MainRoutes.MOVIES) { contentPadding, onFocus ->
                MovieRoute(
                    platform = platform,
                    contentPadding = contentPadding,
                    contentFocusBeacon = contentRequesters[MainRoutes.MOVIES],
                    onFocusLeft = onFocus
                ) { id -> onNavigateToPlayer("movie", id) }
            }
            setupRoute(MainRoutes.SERIES) { contentPadding, onFocus ->
                SerieRoute(
                    platform = platform,
                    contentPadding = contentPadding,
                    contentFocusBeacon = contentRequesters[MainRoutes.SERIES],
                    onFocusLeft = onFocus
                ) { id -> onNavigateToDetail("serie", id) }
            }
            setupRoute(MainRoutes.CHANNELS) { contentPadding, onFocus ->
                ChannelRoute(
                    platform = platform,
                    contentPadding = contentPadding,
                    contentFocusBeacon = contentRequesters[MainRoutes.CHANNELS],
                    onFocusLeft = onFocus
                ) { id -> onNavigateToPlayer("channel", id) }
            }
            setupRoute(MainRoutes.ANIME) { contentPadding, onFocus ->
                AnimeRoute(
                    platform = platform,
                    contentPadding = contentPadding,
                    contentFocusBeacon = contentRequesters[MainRoutes.ANIME],
                    onFocusLeft = onFocus
                ) { id -> onNavigateToDetail("anime", id) }
            }
            setupRoute(MainRoutes.PROFILE) { contentPadding, onFocus ->
                ChannelRoute(
                    platform = platform,
                    contentPadding = contentPadding,
                    contentFocusBeacon = contentRequesters[MainRoutes.PROFILE],
                    onFocusLeft = onFocus
                ) {  }
            }

        }

        TvSideNavigation(
            modifier = Modifier
                .align(Alignment.CenterStart),
            navItems = navItems,
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