package com.jycra.filmaico.core.ui.feature.detail.season

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.jycra.filmaico.core.ui.feature.detail.season.mobile.SeasonDropdown
import com.jycra.filmaico.core.ui.feature.detail.season.tv.SeasonTabs

@Composable
fun SeasonSelector(
    modifier: Modifier = Modifier,
    platform: String,
    seasons: List<UiSeason>,
    onSeasonSelected: (UiSeason) -> Unit
) {

    when (platform) {
        "mobile" -> SeasonDropdown(
            modifier = modifier,
            seasons = seasons,
            onSeasonSelected = onSeasonSelected
        )
        "tv" -> SeasonTabs(
            seasons = seasons,
            onSeasonSelected = onSeasonSelected
        )
    }

}