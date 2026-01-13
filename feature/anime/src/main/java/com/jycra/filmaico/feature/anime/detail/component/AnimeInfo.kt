package com.jycra.filmaico.feature.anime.detail.component

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
import com.jycra.filmaico.core.ui.feature.detail.content.ContentCover
import com.jycra.filmaico.core.ui.feature.detail.content.ContentStatusChip
import com.jycra.filmaico.domain.anime.model.Anime
import com.jycra.filmaico.domain.anime.model.episodeCount
import com.jycra.filmaico.domain.anime.model.localizedName
import com.jycra.filmaico.domain.anime.model.localizedSynopsis

@Composable
internal fun AnimeInfo(
    platform: Platform,
    anime: Anime
) {

    when (platform) {
        Platform.MOBILE -> AnimeInfoMobile(anime = anime)
        Platform.TV -> AnimeInfoTv(anime = anime)
    }

}

@Composable
private fun AnimeInfoMobile(
    anime: Anime
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        ContentCover(
            coverUrl = anime.coverUrl,
            contentDescription = anime.localizedName
        )

        Spacer(modifier = Modifier.width(16.dp))

        Column {

            Text(
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface,
                text = anime.localizedName
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
                text = "${anime.releaseYear} • ${anime.seasons.size} ${stringResource(R.string.seriedetail_text_seasons)}"
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
                text = "${anime.episodeCount} ${stringResource(R.string.seriedetail_text_episodes)}"
            )

            Spacer(modifier = Modifier.height(8.dp))

            ContentStatusChip(
                status = anime.status
            )

        }

    }

}

@Composable
private fun AnimeInfoTv(
    anime: Anime
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
                text = anime.localizedName
            )

            Spacer(modifier = Modifier.height(4.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                Text(
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    text = "${anime.releaseYear}"
                )

                Text(
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    text = "|"
                )

                Text(
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    text = "${anime.seasons.size} ${stringResource(R.string.seriedetail_text_seasons)}"
                )

                Text(
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    text = "|"
                )

                Text(
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    text = "${anime.episodeCount} ${stringResource(R.string.seriedetail_text_episodes)}"
                )

                Text(
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    text = "|"
                )

                ContentStatusChip(
                    status = anime.status
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
                text = anime.localizedSynopsis,
                maxLines = 5,
                overflow = TextOverflow.Ellipsis
            )

        }

        ContentCover(
            modifier = Modifier
                .padding(end = 128.dp),
            coverUrl = anime.coverUrl,
            contentDescription = anime.localizedName
        )

    }

}