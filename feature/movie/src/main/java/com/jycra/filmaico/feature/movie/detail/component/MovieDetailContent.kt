package com.jycra.filmaico.feature.movie.detail.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
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
import com.jycra.filmaico.core.ui.component.FilmaicoLogoSidebar
import com.jycra.filmaico.core.ui.feature.detail.internal.MediaCover
import com.jycra.filmaico.core.ui.feature.detail.internal.Synopsis
import com.jycra.filmaico.domain.media.model.Media
import com.jycra.filmaico.domain.media.util.extesion.localizedImageUrl
import com.jycra.filmaico.domain.media.util.extesion.localizedName
import com.jycra.filmaico.domain.media.util.extesion.localizedSynopsis
import java.util.Calendar

@Composable
fun MovieDetailContent(
    platform: Platform,
    media: Media.Container,
    onStartPlayback: () -> Unit,
    onBackPressed: () -> Unit = {},
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
            FilmaicoLogoSidebar()
        }
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        if (platform == Platform.MOBILE) {

            MediaCover(
                coverUrl = media.localizedImageUrl,
                platform = platform,
                contentDescription = media.localizedName
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
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface,
                text = media.localizedName
            )

            Spacer(modifier = Modifier.height(8.dp))

            val releaseYear = media.airDate?.let { millis ->
                val calendar = Calendar.getInstance().apply { timeInMillis = millis }
                calendar.get(Calendar.YEAR).toString()
            } ?: ""

            Text(
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
                text = releaseYear
            )

            if (platform == Platform.TV) {

                Spacer(modifier = Modifier.height(16.dp))

                Synopsis(
                    platform = platform,
                    synopsis = media.localizedSynopsis
                )

            }

        }

        if (platform == Platform.TV) {
            MediaCover(
                modifier = Modifier
                    .padding(end = 128.dp),
                platform = platform,
                coverUrl = media.localizedImageUrl,
                contentDescription = media.localizedName
            )
        }

    }

    if (platform == Platform.MOBILE) {
        Synopsis(
            platform = platform,
            synopsis = media.localizedSynopsis
        )
    }

    Button(
        modifier = if (platform == Platform.MOBILE) {
            Modifier
                .fillMaxWidth()
        } else Modifier,
        shape = RoundedCornerShape(8.dp),
        onClick = onStartPlayback,
    ) {

        Text(
            style = MaterialTheme.typography.titleSmall,
            text = stringResource(R.string.moviedetail_button_play),
        )

    }

}