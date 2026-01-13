package com.jycra.filmaico.core.ui.feature.detail.season.tv

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jycra.filmaico.core.ui.component.tab.TabItem
import com.jycra.filmaico.core.ui.feature.detail.season.UiSeason

@Composable
fun SeasonTabs(
    seasons: List<UiSeason>,
    onSeasonSelected: (UiSeason) -> Unit
) {

    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(horizontal = 32.dp)
    ) {

        items(seasons) { season ->

            TabItem(
                text = season.title,
                isSelected = season.isSelected,
                onClick = { onSeasonSelected(season) }
            )

        }

    }

}