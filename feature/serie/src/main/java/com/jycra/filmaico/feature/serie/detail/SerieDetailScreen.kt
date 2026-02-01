package com.jycra.filmaico.feature.serie.detail

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.jycra.filmaico.core.device.Platform
import com.jycra.filmaico.core.ui.feature.detail.MediaDetailScaffold
import com.jycra.filmaico.core.ui.feature.media.model.UiMediaSeason
import com.jycra.filmaico.core.ui.feature.media.MediaCarousel
import com.jycra.filmaico.core.ui.feature.media.model.UiMediaCarousel
import com.jycra.filmaico.core.ui.feature.media.model.UiMediaDetail
import com.jycra.filmaico.core.ui.util.focus.MediaFocusCallbacks
import com.jycra.filmaico.core.ui.util.focus.MediaFocusState

@Composable
fun SerieDetailScreen(
    uiState: SerieDetailUiState,
    platform: Platform,
    mediaFocusState: MediaFocusState,
    mediaFocusCallbacks: MediaFocusCallbacks,
    onEvent: (SerieDetailUiEvent) -> Unit
) {

    when (uiState) {
        is SerieDetailUiState.Loading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
        is SerieDetailUiState.Success -> {
            val selectedSeason = uiState.detail.seasons.find { it.isSelected }
                ?: uiState.detail.seasons.firstOrNull()
            Screen(
                platform = platform,
                detail = uiState.detail,
                selectedSeason = selectedSeason,
                mediaFocusState = mediaFocusState,
                mediaFocusCallbacks = mediaFocusCallbacks,
                onSeasonSelected = { seasonId ->
                    onEvent(SerieDetailUiEvent.OnSeasonSelected(seasonId))
                },
                onPlayAsset = { assetId, index ->
                    val asset = uiState.detail.selectedSeasonContents.find { it.id == assetId }
                    asset?.let { asset ->
                        onEvent(
                            SerieDetailUiEvent.PlayAsset(
                                mediaType = asset.mediaType,
                                assetId = asset.id,
                                index = index
                            )
                        )
                    }
                },
                onNavigateBack = {
                    onEvent(SerieDetailUiEvent.OnBackPressed)
                }
            )
        }
        is SerieDetailUiState.Error -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = uiState.message)
            }
        }
    }

}

@Composable
private fun Screen(
    platform: Platform,
    detail: UiMediaDetail,
    selectedSeason: UiMediaSeason?,
    mediaFocusState: MediaFocusState? = null,
    mediaFocusCallbacks: MediaFocusCallbacks? = null,
    onSeasonSelected: (seasonId: String) -> Unit,
    onPlayAsset: (assetId: String, index: Int) -> Unit,
    onNavigateBack: () -> Unit
) {

    val pagerState = rememberPagerState(pageCount = { detail.seasons.size })

    if (platform == Platform.TV) {
        LaunchedEffect(selectedSeason) {
            val seasonIndex = detail.seasons.indexOf(selectedSeason)
            if (seasonIndex != -1 && pagerState.currentPage != seasonIndex) {
                pagerState.animateScrollToPage(seasonIndex)
            }
        }
        LaunchedEffect(pagerState.currentPage) {
            val newSeason = detail.seasons.getOrNull(pagerState.currentPage)
            if (newSeason != null && newSeason != selectedSeason) {
                onSeasonSelected(newSeason.id)
            }
        }
    }

    MediaDetailScaffold(
        platform = platform,
        media = detail,
        onSeasonSelected = { seasonId ->
            detail.seasons.find { it.id == seasonId }?.let { season ->
                onSeasonSelected(season.id)
            }
        },
        onBackPressed = onNavigateBack
    ) { innerPadding ->

        val episodesCarousel = remember(detail.selectedSeasonContents) {
            UiMediaCarousel(
                id = "episodes",
                title = "Episodes",
                items = detail.selectedSeasonContents
            )
        }

        MediaCarousel(
            platform = platform,
            contentPadding = innerPadding,
            carousel = episodesCarousel,
            carouselIndex = 0,
            mediaFocusState = mediaFocusState,
            mediaFocusCallbacks = mediaFocusCallbacks,
            onContentClick = { assetId, mediaType, carouselIndex, contentIndex ->
                onPlayAsset(assetId, contentIndex)
            }
        )

    }

}