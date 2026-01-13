package com.jycra.filmaico.feature.serie.detail

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
import com.jycra.filmaico.feature.serie.detail.component.SerieDetailLayout

@Composable
fun SerieDetailScreen(
    uiState: SerieDetailUiState,
    platform: Platform,
    browseFocusState: BrowseFocusState,
    browseFocusCallbacks: BrowseFocusCallbacks,
    onEvent: (SerieDetailUiEvent) -> Unit
) {

    when (uiState) {
        is SerieDetailUiState.Loading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
        is SerieDetailUiState.Success -> {
            Screen(
                platform = platform,
                state = uiState,
                browseFocusState = browseFocusState,
                browseFocusCallbacks = browseFocusCallbacks,
                onEvent = onEvent
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
    state: SerieDetailUiState.Success,
    platform: Platform,
    browseFocusState: BrowseFocusState? = null,
    browseFocusCallbacks: BrowseFocusCallbacks? = null,
    onEvent: (SerieDetailUiEvent) -> Unit
) {

    val seasons = state.serie.seasons
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
                onEvent(SerieDetailUiEvent.OnSeasonSelected(newSeason))
            }
        }
    }

    SerieDetailLayout(
        state = state,
        platform = platform,
        browseFocusState = browseFocusState,
        browseFocusCallbacks = browseFocusCallbacks,
        onEvent = onEvent
    )

}