package com.jycra.filmaico.feature.movie.detail.component

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
import com.jycra.filmaico.domain.movie.model.Movie
import com.jycra.filmaico.domain.movie.model.localizedName
import com.jycra.filmaico.domain.movie.model.localizedSynopsis

@Composable
fun MovieInfo(
    platform: Platform,
    movie: Movie
) {

    when (platform) {
        Platform.MOBILE -> MovieInfoMobile(movie = movie)
        Platform.TV -> MovieInfoTv(movie = movie)
    }

}

@Composable
fun MovieInfoMobile(
    movie: Movie
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        ContentCover(
            coverUrl = movie.coverUrl,
            contentDescription = movie.localizedName
        )

        Spacer(modifier = Modifier.width(16.dp))

        Column {

            Text(
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface,
                text = movie.localizedName
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
                text = "${movie.releaseYear}"
            )

        }

    }

}

@Composable
fun MovieInfoTv(
    movie: Movie
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
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
                text = movie.localizedName
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
                text = "${movie.releaseYear}"
            )

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
                text = movie.localizedSynopsis,
                maxLines = 5,
                overflow = TextOverflow.Ellipsis
            )

        }

        ContentCover(
            modifier = Modifier
                .padding(end = 128.dp),
            coverUrl = movie.coverUrl,
            contentDescription = movie.localizedName
        )

    }

}