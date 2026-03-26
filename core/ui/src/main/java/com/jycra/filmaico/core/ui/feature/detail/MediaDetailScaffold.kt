package com.jycra.filmaico.core.ui.feature.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jycra.filmaico.core.device.Platform
import com.jycra.filmaico.core.ui.feature.detail.internal.SurroundingBackgroundTopCenter
import com.jycra.filmaico.core.ui.feature.detail.internal.SurroundingBackgroundTopEnd
import com.jycra.filmaico.core.ui.feature.media.model.UiMediaDetail
import com.jycra.filmaico.core.ui.theme.color.Gradient

@Composable
fun MediaDetailScaffold(
    platform: Platform,
    media: UiMediaDetail,
    onSeasonSelected: (seasonId: String) -> Unit,
    onBackPressed: () -> Unit,
    content: @Composable (innerPadding: PaddingValues) -> Unit
) {

    when (platform) {
        Platform.MOBILE -> {
            Scaffold(
                topBar = {

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(brush = Gradient.verticalDetailGradient())
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null,
                                onClick = {}
                            ),
                        contentAlignment = Alignment.TopCenter
                    ) {

                        SurroundingBackgroundTopCenter(backgroundUrl = media.imageUrl)

                        MediaDetailHeader(
                            platform = platform,
                            media = media,
                            onSeasonSelected = onSeasonSelected,
                            onBackPressed = onBackPressed
                        )

                    }
                },
                containerColor = MaterialTheme.colorScheme.surfaceContainerLowest
            ) { innerPadding ->

                content(innerPadding)

            }
        }
        Platform.TV -> {

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = MaterialTheme.colorScheme.surfaceContainerLowest),
                contentAlignment = Alignment.TopEnd
            ) {

                SurroundingBackgroundTopEnd(backgroundUrl = media.imageUrl)

                Column(modifier = Modifier.fillMaxSize()) {

                    MediaDetailHeader(
                        platform = platform,
                        media = media,
                        onSeasonSelected = onSeasonSelected,
                        onBackPressed = onBackPressed
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    content(PaddingValues(horizontal = 32.dp))

                }

            }

        }

    }

}