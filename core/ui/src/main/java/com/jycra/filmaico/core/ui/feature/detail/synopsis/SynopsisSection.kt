package com.jycra.filmaico.core.ui.feature.detail.synopsis

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.jycra.filmaico.core.ui.R

@Composable
fun SynopsisSection(
    modifier: Modifier = Modifier,
    synopsis: String
) {

    var showFullSynopsis by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .padding(vertical = 16.dp)
    ) {

        Text(
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface,
            text = stringResource(R.string.ui_component_section_synopsis_title)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.72f),
            text = synopsis,
            maxLines = if (showFullSynopsis) Int.MAX_VALUE else 3,
            overflow = TextOverflow.Ellipsis
        )

        if (synopsis.lines().size > 3 || synopsis.length > 150) {

            Text(
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(all = 4.dp)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = { showFullSynopsis = !showFullSynopsis }
                    ),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface,
                text = if (!showFullSynopsis)
                    stringResource(R.string.ui_component_section_synopsis_button_show_more)
                else
                    stringResource(R.string.ui_component_section_synopsis_button_show_less)
            )

        }

    }

}