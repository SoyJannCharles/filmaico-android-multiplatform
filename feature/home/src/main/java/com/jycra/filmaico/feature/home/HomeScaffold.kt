package com.jycra.filmaico.feature.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import com.jycra.filmaico.core.device.Platform
import com.jycra.filmaico.domain.media.model.MediaType
import com.jycra.filmaico.feature.anime.AnimeRoute
import com.jycra.filmaico.feature.channel.ChannelRoute
import com.jycra.filmaico.feature.home.common.HomeTabs
import com.jycra.filmaico.feature.home.components.HomeTopHeader
import com.jycra.filmaico.feature.movie.MovieRoute
import com.jycra.filmaico.feature.serie.SerieRoute
import kotlinx.coroutines.launch

@Composable
fun HomeScaffoldMobile(
    uiState: HomeUiState,
    platform: Platform,
    mainScaffoldPadding: PaddingValues,
    onEvent: (HomeUiEvent) -> Unit
) {

    val tabs = remember { HomeTabs.entries.toList() }
    val pagerState = rememberPagerState { tabs.size }

    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            HomeTopHeader(
                uiState = uiState,
                tabs = tabs,
                selectedTabIndex = pagerState.currentPage,
                onTabSelected = { index ->
                    scope.launch { pagerState.animateScrollToPage(index) }
                },
                onProfileClick = { onEvent(HomeUiEvent.OnProfileClick) }
            )
        },
        containerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
        contentWindowInsets = WindowInsets(0, 0, 0, 0)
    ) { innerPadding ->

        val contentPadding = PaddingValues(
            top = innerPadding.calculateTopPadding(),
            bottom = mainScaffoldPadding.calculateBottomPadding()
        )

        Box(modifier = Modifier.fillMaxSize()) {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxSize(),
                userScrollEnabled = false
            ) { pageIndex ->

                when(tabs[pageIndex]) {
                    HomeTabs.ALL -> ChannelRoute(
                        platform = platform,
                        contentPadding = contentPadding,
                        onPlayAsset = { assetId -> onEvent(HomeUiEvent.PlayAsset(
                            mediaType = MediaType.CHANNEL,
                            assetId = assetId
                        )) }
                    )
                    HomeTabs.MOVIES -> MovieRoute(
                        platform = platform,
                        contentPadding = contentPadding,
                        onOpenDetail = { containerId -> onEvent(HomeUiEvent.OpenDetail(
                            mediaType = MediaType.MOVIE,
                            containerId = containerId
                        )) },
                        onPlayAsset = { mediaType, assetId -> onEvent(HomeUiEvent.PlayAsset(
                            mediaType = mediaType,
                            assetId = assetId
                        )) }
                    )
                    HomeTabs.SERIES -> SerieRoute(
                        platform = platform,
                        contentPadding = contentPadding,
                        onOpenDetail = { containerId -> onEvent(HomeUiEvent.OpenDetail(
                            mediaType = MediaType.SERIE,
                            containerId = containerId
                        )) },
                        onPlayAsset = { mediaType, assetId -> onEvent(HomeUiEvent.PlayAsset(
                            mediaType = mediaType,
                            assetId = assetId
                        )) }
                    )
                    HomeTabs.CHANNELS -> ChannelRoute(
                        platform = platform,
                        contentPadding = contentPadding,
                        onPlayAsset = { assetId -> onEvent(HomeUiEvent.PlayAsset(
                            mediaType = MediaType.CHANNEL,
                            assetId = assetId
                        )) }
                    )
                    HomeTabs.ANIME -> AnimeRoute(
                        platform = platform,
                        contentPadding = contentPadding,
                        onOpenDetail = { containerId -> onEvent(HomeUiEvent.OpenDetail(
                            mediaType = MediaType.ANIME,
                            containerId = containerId
                        )) },
                        onPlayAsset = { mediaType, assetId -> onEvent(HomeUiEvent.PlayAsset(
                            mediaType = mediaType,
                            assetId = assetId
                        )) }
                    )
                }

            }

        }

    }

}

