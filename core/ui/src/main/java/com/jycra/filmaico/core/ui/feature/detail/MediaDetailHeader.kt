package com.jycra.filmaico.core.ui.feature.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.jycra.filmaico.core.device.Platform
import com.jycra.filmaico.core.ui.R
import com.jycra.filmaico.core.ui.component.FilmaicoLogo
import com.jycra.filmaico.core.ui.feature.detail.internal.MediaCover
import com.jycra.filmaico.core.ui.feature.detail.internal.MediaStatusChip
import com.jycra.filmaico.core.ui.feature.detail.internal.MediaSeasonSelector
import com.jycra.filmaico.core.ui.feature.detail.internal.Synopsis
import com.jycra.filmaico.core.ui.feature.media.model.UiMediaDetail

@Composable
fun MediaDetailHeader(
    platform: Platform,
    media: UiMediaDetail,
    onSeasonSelected: (String) -> Unit,
    onBackPressed: () -> Unit
) {

    Column(
        modifier = if (platform == Platform.MOBILE) {
            Modifier
                .statusBarsPadding()
                .padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
        } else {
            Modifier
                .fillMaxWidth()
                .padding(
                    top = 32.dp,
                    start = 32.dp,
                    end = 32.dp
                )
        }
    ) {

        when (platform) {
            Platform.MOBILE -> {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    IconButton(onClick = onBackPressed) {
                        Icon(
                            modifier = Modifier.size(24.dp),
                            tint = MaterialTheme.colorScheme.onSurface,
                            painter = painterResource(R.drawable.ic_arrow_back),
                            contentDescription = "Back"
                        )
                    }

                    FilmaicoLogo(modifier = Modifier.weight(1f))

                    Spacer(modifier = Modifier.width(48.dp))

                }
            }
            Platform.TV -> {
                FilmaicoLogo()
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = if (platform == Platform.MOBILE) 16.dp else 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            if (platform == Platform.MOBILE) {
                MediaCover(
                    coverUrl = media.imageUrl,
                    platform = platform,
                    contentDescription = media.name
                )

                Spacer(modifier = Modifier.width(16.dp))
            }

            Column(
                modifier = if (platform == Platform.TV) {
                    Modifier
                        .weight(1f)
                        .padding(end = 128.dp)
                } else Modifier
            ) {

                Text(
                    style = if (platform == Platform.MOBILE)
                        MaterialTheme.typography.titleLarge
                    else
                        MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    text = media.name
                )

                Spacer(modifier = Modifier.height(8.dp))

                if (platform == Platform.MOBILE) {
                    Text(
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                        text = "${media.releaseYear} • ${media.seasonCount} ${stringResource(R.string.seriedetail_text_seasons)}"
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                        text = "${media.episodeCount} ${stringResource(R.string.seriedetail_text_episodes)}"
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    MediaStatusChip(
                        status = media.status
                    )

                } else {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {

                        Text(
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface,
                            text = media.releaseYear
                        )

                        Text(
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface,
                            text = "|"
                        )

                        Text(
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface,
                            text = "${media.seasonCount} ${stringResource(R.string.seriedetail_text_seasons)}"
                        )

                        Text(
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface,
                            text = "|"
                        )

                        Text(
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface,
                            text = "${media.episodeCount} ${stringResource(R.string.seriedetail_text_episodes)}"
                        )

                        Text(
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface,
                            text = "|"
                        )

                        MediaStatusChip(
                            status = media.status
                        )

                    }

                }

                if (platform == Platform.TV) {
                    Synopsis(
                        platform = platform,
                        synopsis = media.synopsis
                    )
                }

            }

            if (platform == Platform.TV) {
                MediaCover(
                    modifier = Modifier
                        .padding(end = 128.dp),
                    platform = platform,
                    coverUrl = media.imageUrl,
                    contentDescription = media.name
                )
            }

        }

        if (platform == Platform.MOBILE) {

            Synopsis(
                platform = platform,
                synopsis = media.synopsis
            )

            MediaSeasonSelector(
                modifier = Modifier.fillMaxWidth(),
                platform = platform,
                seasons = media.seasons,
                onSeasonSelected = { season ->
                    onSeasonSelected(season.id)
                }
            )

        }

    }

    if (platform == Platform.TV) {
        MediaSeasonSelector(
            modifier = Modifier.fillMaxWidth(),
            platform = platform,
            seasons = media.seasons,
            onSeasonSelected = { season ->
                onSeasonSelected(season.id)
            }
        )
    }

}