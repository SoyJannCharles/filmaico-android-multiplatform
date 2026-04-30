package com.jycra.filmaico.core.ui.feature.media.internal

import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.jycra.filmaico.core.ui.feature.media.model.UiMedia
import com.jycra.filmaico.core.ui.feature.media.util.variant.MediaCardVariant
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun MediaMetadata(
    media: UiMedia,
    variant: MediaCardVariant
) {

    Column(
        modifier = Modifier
            .padding(top = 8.dp, start = 4.dp, end = 4.dp)
    ) {

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(
                text = if (media.epgTitle != null) "${media.name} |" else media.name,
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = if (variant == MediaCardVariant.POSTER_VERTICAL) 1 else 2,
                overflow = TextOverflow.Ellipsis
            )

            if (media.epgTitle != null) {

                Text(
                    text = " ${media.epgTitle}",
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    modifier = Modifier.basicMarquee(
                        iterations = Int.MAX_VALUE,
                        repeatDelayMillis = 2000
                    )
                )

            } else {

                Text(
                    text = "Sin información",
                    style = MaterialTheme.typography.titleSmall,
                    color = Color.Transparent,
                    maxLines = 1
                )

            }

        }

        media.label?.let {

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = media.label,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.72f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

        }

        if (media.epgTitle != null) {

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = media.epgTimeLabel,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.72f),
                maxLines = 1,
                modifier = Modifier.basicMarquee(
                    iterations = Int.MAX_VALUE,
                    repeatDelayMillis = 2000
                )
            )

        } else {

            Text(
                text = "Sin horario",
                style = MaterialTheme.typography.bodySmall,
                color = Color.Transparent,
                maxLines = 1
            )

        }

    }

}