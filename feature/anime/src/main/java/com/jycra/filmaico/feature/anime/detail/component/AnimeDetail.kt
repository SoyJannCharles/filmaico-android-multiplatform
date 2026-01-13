package com.jycra.filmaico.feature.anime.detail.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jycra.filmaico.core.navigation.Platform
import com.jycra.filmaico.core.ui.component.FilmaicoLogo
import com.jycra.filmaico.core.ui.feature.detail.background.mobile.SurroundingBackgroundTopCenter
import com.jycra.filmaico.core.ui.feature.detail.navigation.DetailTopBar
import com.jycra.filmaico.core.ui.feature.detail.season.SeasonSelector
import com.jycra.filmaico.core.ui.feature.detail.season.UiSeason
import com.jycra.filmaico.core.ui.feature.detail.synopsis.SynopsisSection
import com.jycra.filmaico.core.ui.theme.color.Gradient
import com.jycra.filmaico.domain.anime.model.Anime
import com.jycra.filmaico.domain.anime.model.AnimeSeason
import com.jycra.filmaico.domain.anime.model.localizedName
import com.jycra.filmaico.domain.anime.model.localizedSynopsis
import com.jycra.filmaico.feature.anime.detail.AnimeDetailUiEvent

@Composable
fun MobileDetail(
    anime: Anime,
    selectedSeason: AnimeSeason,
    onEvent: (AnimeDetailUiEvent) -> Unit
) {

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(brush = Gradient.verticalDetailGradient())
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = {}
            ),
        contentAlignment = Alignment.TopCenter
    ) {

        SurroundingBackgroundTopCenter(backgroundUrl = anime.coverUrl)

        Column(
            modifier = Modifier
                .statusBarsPadding()
                .padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
        ) {

            DetailTopBar(
                onBackPressed = { onEvent(AnimeDetailUiEvent.OnBackPressed) }
            )

            AnimeInfo(
                platform = Platform.MOBILE,
                anime = anime
            )

            SynopsisSection(synopsis = anime.localizedSynopsis)

            SeasonSelector(
                modifier = Modifier.fillMaxWidth(),
                platform = Platform.MOBILE.platform,
                seasons = anime.seasons.map { season ->
                    UiSeason(
                        id = season.id,
                        title = season.localizedName,
                        isSelected = season == selectedSeason
                    )
                },
                onSeasonSelected = { uiSeason ->
                    anime.seasons.firstOrNull { it.id == uiSeason.id }
                        ?.let { realSeason ->
                            onEvent(AnimeDetailUiEvent.OnSeasonSelected(realSeason))
                        }
                }
            )

        }

    }

}

@Composable
fun TvDetail(
    anime: Anime,
    selectedSeason: AnimeSeason,
    onEvent: (AnimeDetailUiEvent) -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = 32.dp)
        ) {

            FilmaicoLogo()

            AnimeInfo(
                platform = Platform.TV,
                anime = anime
            )

        }

        SeasonSelector(
            platform = Platform.TV.platform,
            seasons = anime.seasons.map { season ->
                UiSeason(
                    id = season.id,
                    title = season.localizedName,
                    isSelected = season == selectedSeason
                )
            },
            onSeasonSelected = { uiSeason ->
                anime.seasons.firstOrNull { it.id == uiSeason.id }
                    ?.let { season ->
                        onEvent(AnimeDetailUiEvent.OnSeasonSelected(season))
                    }
            }
        )

    }

}