package com.jycra.filmaico.core.ui.feature.detail.season.mobile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
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
import com.jycra.filmaico.core.ui.R
import com.jycra.filmaico.core.ui.feature.detail.season.UiSeason

@Composable
fun SeasonDropdown(
    modifier: Modifier = Modifier,
    seasons: List<UiSeason>,
    onSeasonSelected: (UiSeason) -> Unit
) {

    var expanded by remember { mutableStateOf(false) }

    val selectedSeason = seasons.firstOrNull { it.isSelected }

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