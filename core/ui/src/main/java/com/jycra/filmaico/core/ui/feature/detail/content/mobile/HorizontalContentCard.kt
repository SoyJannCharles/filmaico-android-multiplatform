package com.jycra.filmaico.core.ui.feature.detail.content.mobile

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.jycra.filmaico.core.ui.feature.browse.util.CardDimensions
import com.jycra.filmaico.core.ui.feature.detail.content.UiContent
import com.jycra.filmaico.core.ui.feature.detail.content.formattedDuration

@Composable
fun HorizontalContentCard(
    content: UiContent,
    contentType: String,
    dimensions: CardDimensions,
    onContentClick: () -> Unit
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLowest
        ),
        onClick = onContentClick
    ) {

        Row(verticalAlignment = Alignment.CenterVertically) {

            AsyncImage(
                modifier = Modifier
                    .width(dimensions.width)
                    .height(dimensions.height)
                    .clip(RoundedCornerShape(8.dp)),
                model = content.thumbnailUrl,
                contentDescription = content.name,
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {

                Text(
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onSurface,
                    text = content.name,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(0.72f),
                    text = "$contentType ${content.order} · ${content.formattedDuration}"
                )

            }

        }

    }

}