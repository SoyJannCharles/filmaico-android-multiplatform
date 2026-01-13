package com.jycra.filmaico.feature.anime.detail.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jycra.filmaico.core.navigation.Platform
import com.jycra.filmaico.core.ui.feature.detail.background.tv.SurroundingBackgroundTopEnd
import com.jycra.filmaico.core.ui.feature.detail.content.ContentList
import com.jycra.filmaico.core.ui.feature.detail.content.UiContent
import com.jycra.filmaico.core.ui.util.focus.BrowseFocusCallbacks
import com.jycra.filmaico.core.ui.util.focus.BrowseFocusState
import com.jycra.filmaico.domain.anime.model.localizedName
import com.jycra.filmaico.feature.anime.detail.AnimeDetailUiEvent
import com.jycra.filmaico.feature.anime.detail.AnimeDetailUiState

@Composable
fun AnimeDetailLayout(
    state: AnimeDetailUiState.Success,
    platform: Platform,
    browseFocusState: BrowseFocusState? = null,
    browseFocusCallbacks: BrowseFocusCallbacks? = null,
    onEvent: (AnimeDetailUiEvent) -> Unit
) {

    when (platform) {
        Platform.MOBILE -> MobileLayout(
            state = state,
            onEvent = onEvent
        )
        Platform.TV -> TvLayout(
            state = state,
            browseFocusState = browseFocusState!!,
            browseFocusCallbacks = browseFocusCallbacks!!,
            onEvent = onEvent
        )
    }

}

@Composable
private fun MobileLayout(
    state: AnimeDetailUiState.Success,
    onEvent: (AnimeDetailUiEvent) -> Unit
) {

    Scaffold(
        topBar = {
            MobileDetail(
                anime = state.anime,
                selectedSeason = state.selectedSeason,
                onEvent = onEvent
            )
        },
        containerColor = MaterialTheme.colorScheme.surfaceContainerLowest
    ) { innerPadding ->

        ContentList(
            platform = Platform.MOBILE,
            contentPadding = innerPadding,
            contentList = state.contentsForSeason.map { content ->
                UiContent(
                    id = content.id,
                    type = content.type,
                    thumbnailUrl = content.thumbnailUrl,
                    name = content.localizedName,
                    duration = content.duration,
                    order = content.order
                )
            },
            onContentClick = { uiContent, index ->
                state.contentsForSeason.firstOrNull { it.id == uiContent.id }
                    ?.let { content ->
                        onEvent(AnimeDetailUiEvent.OnContentClick(content))
                    }
            }
        )

    }

}

@Composable
private fun TvLayout(
    state: AnimeDetailUiState.Success,
    browseFocusState: BrowseFocusState,
    browseFocusCallbacks: BrowseFocusCallbacks,
    onEvent: (AnimeDetailUiEvent) -> Unit
) {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.surfaceContainerLowest),
        contentAlignment = Alignment.TopEnd
    ) {

        SurroundingBackgroundTopEnd(state.anime.coverUrl)

        Column(modifier = Modifier.fillMaxSize()) {

            TvDetail(
                anime = state.anime,
                selectedSeason = state.selectedSeason,
                onEvent = onEvent
            )

            Spacer(modifier = Modifier.height(16.dp))

            ContentList(
                platform = Platform.TV,
                contentList = state.contentsForSeason.map { content ->
                    UiContent(
                        id = content.id,
                        type = content.type,
                        thumbnailUrl = content.thumbnailUrl,
                        name = content.localizedName,
                        duration = content.duration,
                        order = content.order
                    )
                },
                browseFocusState = browseFocusState,
                browseFocusCallbacks = browseFocusCallbacks,
                onContentClick = { uiContent, index ->
                    state.contentsForSeason.firstOrNull { it.id == uiContent.id }
                        ?.let { content ->
                            onEvent(AnimeDetailUiEvent.OnContentClick(content, index))
                        }
                }
            )

        }

    }

}