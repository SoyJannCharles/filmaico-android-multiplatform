package com.jycra.filmaico.core.ui.feature.detail.content.tv

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.jycra.filmaico.core.ui.component.card.OptimizedCard
import com.jycra.filmaico.core.ui.feature.browse.util.CardDimensions
import com.jycra.filmaico.core.ui.feature.detail.content.UiContent
import com.jycra.filmaico.core.ui.feature.detail.content.formattedDuration

@Composable
fun VerticalContentCard(
    content: UiContent,
    dimensions: CardDimensions,
    contentType: String,
    focusRequester: FocusRequester,
    onContentClick: () -> Unit
) {

    Column(
        modifier = Modifier.width(dimensions.width),
        horizontalAlignment = Alignment.Start
    ) {

        val isSelected = remember { mutableStateOf(false) }

        OptimizedCard(
            modifier = Modifier
                .fillMaxWidth()
                .height(dimensions.height)
                .focusRequester(focusRequester),
            selected = isSelected,
            onClick = onContentClick
        ) {
            AsyncImage(
                modifier = Modifier.fillMaxSize(),
                model = ImageRequest.Builder(LocalContext.current)
                    .data(content.thumbnailUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = content.name,
                contentScale = ContentScale.Crop
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

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