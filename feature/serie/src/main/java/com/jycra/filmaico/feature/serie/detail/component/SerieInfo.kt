package com.jycra.filmaico.feature.serie.detail.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.jycra.filmaico.core.navigation.Platform
import com.jycra.filmaico.core.ui.R
import com.jycra.filmaico.core.ui.feature.detail.content.ContentStatusChip
import com.jycra.filmaico.core.ui.feature.detail.content.ContentCover
import com.jycra.filmaico.domain.serie.model.Serie
import com.jycra.filmaico.domain.serie.model.episodeCount
import com.jycra.filmaico.domain.serie.model.localizedName
import com.jycra.filmaico.domain.serie.model.localizedSynopsis

@Composable
internal fun SerieInfo(
    platform: Platform,
    serie: Serie
) {

    when (platform) {
        Platform.MOBILE -> SerieInfoMobile(serie = serie)
        Platform.TV -> SerieInfoTv(serie = serie)
    }

}

@Composable
private fun SerieInfoMobile(
    serie: Serie
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        ContentCover(
            coverUrl = serie.coverUrl,
            contentDescription = serie.localizedName
        )

        Spacer(modifier = Modifier.width(16.dp))

        Column {

            Text(
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface,
                text = serie.localizedName
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
                text = "${serie.releaseYear} • ${serie.seasons.size} ${stringResource(R.string.seriedetail_text_seasons)}"
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
                text = "${serie.episodeCount} ${stringResource(R.string.seriedetail_text_episodes)}"
            )

            Spacer(modifier = Modifier.height(8.dp))

            ContentStatusChip(
                status = serie.status
            )

        }

    }

}

@Composable
private fun SerieInfoTv(
    serie: Serie
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(end = 128.dp)
        ) {

            Text(
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onSurface,
                text = serie.localizedName
            )

            Spacer(modifier = Modifier.height(4.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                Text(
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    text = "${serie.releaseYear}"
                )

                Text(
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    text = "|"
                )

                Text(
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    text = "${serie.seasons.size} ${stringResource(R.string.seriedetail_text_seasons)}"
                )

                Text(
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    text = "|"
                )

                Text(
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    text = "${serie.episodeCount} ${stringResource(R.string.seriedetail_text_episodes)}"
                )

                Text(
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    text = "|"
                )

                ContentStatusChip(
                    status = serie.status
                )


            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
                text = stringResource(R.string.ui_component_section_synopsis_title)
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(0.48f),
                text = serie.localizedSynopsis,
                maxLines = 5,
                overflow = TextOverflow.Ellipsis
            )

        }

        ContentCover(
            modifier = Modifier
                .padding(end = 128.dp),
            coverUrl = serie.coverUrl,
            contentDescription = serie.localizedName
        )

    }

}