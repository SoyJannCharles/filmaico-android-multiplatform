package com.jycra.filmaico.feature.player.components.settings

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.jycra.filmaico.core.ui.R
import com.jycra.filmaico.domain.media.model.stream.Stream
import com.jycra.filmaico.domain.stream.model.metadata.ProviderMetadata

@Composable
fun ProviderSettingsSheet(
    focusRequester: FocusRequester,
    providers: List<Stream>,
    currentProvider: Stream?,
    analysis: Map<String, ProviderMetadata>,
    onProviderSelected: (Stream) -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {

        ProviderSheetHeader(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 16.dp),
            currentProvider = currentProvider
        )

        LazyColumn {
            itemsIndexed(providers) { index, provider ->

                val url = when(provider) {
                    is Stream.Direct -> provider.uri
                    is Stream.WebViewScrap -> provider.iframeUrl
                }

                ProviderItem(
                    provider = provider,
                    isSelected = provider == currentProvider,
                    metadata = analysis[url],
                    modifier = if (index == 0) Modifier.focusRequester(focusRequester) else Modifier,
                    onClick = { onProviderSelected(provider) }
                )

            }
        }

    }

}

@Composable
private fun ProviderSheetHeader(
    modifier: Modifier,
    currentProvider: Stream?
) {

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {

        val displayText = currentProvider?.provider ?: "Por defecto"

        Text(
            text = "Proveedor · $displayText",
            style = MaterialTheme.typography.bodyLarge,
            color = Color.White
        )
    }

}

@Composable
private fun ProviderItem(
    provider: Stream,
    isSelected: Boolean,
    metadata: ProviderMetadata?,
    modifier: Modifier,
    onClick: () -> Unit
) {

    var isFocused by remember { mutableStateOf(false) }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = if (isFocused) MaterialTheme.colorScheme.surfaceContainerLow.copy(0.64f) else Color.Transparent,
                shape = RoundedCornerShape(8.dp)
            )
            .onFocusChanged { isFocused = it.isFocused }
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            )
            .padding(horizontal = 16.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(end = 16.dp)
        ) {

            Text(
                text = provider?.provider ?: "Desconocido",
                style = MaterialTheme.typography.bodyLarge,
                color = Color.White
            )

            AnimatedContent(
                targetState = metadata,
                label = "analysis_fade"
            ) { data ->
                if (data != null) {
                    val extraText = if (data.qualityCount > 1) " · +${data.qualityCount - 1} calidades" else ""
                    Text(
                        text = "${data.mainResolution}$extraText",
                        style = MaterialTheme.typography.labelMedium,
                        color = Color.White.copy(alpha = 0.6f)
                    )
                } else {
                    Text(
                        text = "Verificando calidad...",
                        style = MaterialTheme.typography.labelMedium,
                        color = Color.White.copy(alpha = 0.3f)
                    )
                }
            }

        }

        if (isSelected) {
            Icon(
                painter = painterResource(R.drawable.ic_check),
                contentDescription = "Seleccionado",
                tint = Color.White
            )
        }

    }

}