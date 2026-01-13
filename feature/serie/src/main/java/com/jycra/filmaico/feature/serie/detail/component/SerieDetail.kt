package com.jycra.filmaico.feature.serie.detail.component

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
import com.jycra.filmaico.domain.serie.model.Serie
import com.jycra.filmaico.domain.serie.model.SerieSeason
import com.jycra.filmaico.domain.serie.model.localizedName
import com.jycra.filmaico.domain.serie.model.localizedSynopsis
import com.jycra.filmaico.feature.serie.detail.SerieDetailUiEvent

@Composable
fun MobileDetail(
    serie: Serie,
    selectedSeason: SerieSeason,
    onEvent: (SerieDetailUiEvent) -> Unit
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

        SurroundingBackgroundTopCenter(backgroundUrl = serie.coverUrl)

        Column(
            modifier = Modifier
                .statusBarsPadding()
                .padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
        ) {

            DetailTopBar(
                onBackPressed = { onEvent(SerieDetailUiEvent.OnBackPressed) }
            )

            SerieInfo(
                platform = Platform.MOBILE,
                serie = serie
            )

            SynopsisSection(synopsis = serie.localizedSynopsis)

            SeasonSelector(
                modifier = Modifier.fillMaxWidth(),
                platform = Platform.MOBILE.platform,
                seasons = serie.seasons.map { season ->
                    UiSeason(
                        id = season.id,
                        title = season.localizedName,
                        isSelected = season == selectedSeason
                    )
                },
                onSeasonSelected = { uiSeason ->
                    serie.seasons.firstOrNull { it.id == uiSeason.id }
                        ?.let { realSeason ->
                            onEvent(SerieDetailUiEvent.OnSeasonSelected(realSeason))
                        }
                }
            )

        }

    }

}

@Composable
fun TvDetail(
    serie: Serie,
    selectedSeason: SerieSeason,
    onEvent: (SerieDetailUiEvent) -> Unit
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

            SerieInfo(
                platform = Platform.TV,
                serie = serie
            )

        }

        SeasonSelector(
            platform = Platform.TV.platform,
            seasons = serie.seasons.map { season ->
                UiSeason(
                    id = season.id,
                    title = season.localizedName,
                    isSelected = season == selectedSeason
                )
            },
            onSeasonSelected = { uiSeason ->
                serie.seasons.firstOrNull { it.id == uiSeason.id }
                    ?.let { season ->
                        onEvent(SerieDetailUiEvent.OnSeasonSelected(season))
                    }
            }
        )

    }

}