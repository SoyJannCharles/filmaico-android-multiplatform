package com.jycra.filmaico.feature.anime.detail

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.jycra.filmaico.core.navigation.Platform
import com.jycra.filmaico.core.ui.util.focus.BrowseFocusCallbacks
import com.jycra.filmaico.core.ui.util.focus.BrowseFocusState
import com.jycra.filmaico.feature.anime.detail.component.AnimeDetailLayout

@Composable
fun AnimeDetailScreen(
    uiState: AnimeDetailUiState,
    platform: Platform,
    browseFocusState: BrowseFocusState,
    browseFocusCallbacks: BrowseFocusCallbacks,
    onEvent: (AnimeDetailUiEvent) -> Unit
) {

    when (uiState) {
        is AnimeDetailUiState.Loading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
        is AnimeDetailUiState.Success -> {
            Screen(
                platform = platform,
                state = uiState,
                browseFocusState = browseFocusState,
                browseFocusCallbacks = browseFocusCallbacks,
                onEvent = onEvent
            )
        }
        is AnimeDetailUiState.Error -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = uiState.message)
            }
        }
    }

}

@Composable
private fun Screen(
    state: AnimeDetailUiState.Success,
    platform: Platform,
    browseFocusState: BrowseFocusState? = null,
    browseFocusCallbacks: BrowseFocusCallbacks? = null,
    onEvent: (AnimeDetailUiEvent) -> Unit
) {

    val seasons = state.anime.seasons
    val pagerState = rememberPagerState(pageCount = { seasons.size })

    if (platform == Platform.TV) {
        LaunchedEffect(state.selectedSeason) {
            val seasonIndex = seasons.indexOf(state.selectedSeason)
            if (seasonIndex != -1 && pagerState.currentPage != seasonIndex) {
                pagerState.animateScrollToPage(seasonIndex)
            }
        }
        LaunchedEffect(pagerState.currentPage) {
            val newSeason = seasons.getOrNull(pagerState.currentPage)
            if (newSeason != null && newSeason != state.selectedSeason) {
                onEvent(AnimeDetailUiEvent.OnSeasonSelected(newSeason))
            }
        }
    }

    AnimeDetailLayout(
        state = state,
        platform = platform,
        browseFocusState = browseFocusState,
        browseFocusCallbacks = browseFocusCallbacks,
        onEvent = onEvent
    )

}