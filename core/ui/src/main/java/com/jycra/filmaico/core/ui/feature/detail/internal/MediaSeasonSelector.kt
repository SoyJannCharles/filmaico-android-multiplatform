package com.jycra.filmaico.core.ui.feature.detail.internal

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.jycra.filmaico.core.device.Platform
import com.jycra.filmaico.core.ui.R
import com.jycra.filmaico.core.ui.component.tab.TabItem
import com.jycra.filmaico.core.ui.feature.media.model.UiMediaSeason

@Composable
fun MediaSeasonSelector(
    modifier: Modifier = Modifier,
    platform: Platform,
    seasons: List<UiMediaSeason>,
    onSeasonSelected: (UiMediaSeason) -> Unit
) {

    when (platform) {
        Platform.MOBILE -> {

            val selectedSeason = seasons.firstOrNull { it.isSelected }
            var expanded by remember { mutableStateOf(false) }

            ExposedDropdownMenuBox(
                modifier = modifier,
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }
            ) {

                Row(
                    modifier = Modifier
                        .menuAnchor()
                        .padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Text(
                        modifier = Modifier
                            .weight(1f, false),
                        style = MaterialTheme.typography.titleMedium,
                        text = selectedSeason?.title ?: "",
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )

                    Icon(
                        painter = painterResource(R.drawable.ic_arrow_drop_down),
                        contentDescription = "Seleccionar temporada"
                    )

                }

                ExposedDropdownMenu(
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.surfaceContainerLow),
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {

                    seasons.forEach { season ->

                        DropdownMenuItem(
                            text = { Text(season.title) },
                            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                            onClick = {
                                onSeasonSelected(season)
                                expanded = false
                            }
                        )

                    }

                }

            }

        }
        Platform.TV -> {

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
    }

}