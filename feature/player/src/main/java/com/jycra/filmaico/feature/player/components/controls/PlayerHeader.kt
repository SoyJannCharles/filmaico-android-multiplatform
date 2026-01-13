package com.jycra.filmaico.feature.player.components.controls

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.jycra.filmaico.core.navigation.Platform
import com.jycra.filmaico.core.ui.R
import com.jycra.filmaico.feature.player.model.VideoMetadata

@Composable
fun PlayerHeader(
    modifier: Modifier = Modifier,
    platform: Platform,
    headerInfo: VideoMetadata,
    onBackClick: () -> Unit
) {

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        if (platform == Platform.MOBILE) {
            IconButton(onClick = onBackClick) {
                Icon(
                    painter = painterResource(R.drawable.ic_arrow_back),
                    contentDescription = "Volver",
                    tint = Color.White
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
        }

        Column {

            Text(
                text = headerInfo.title,
                style = if (platform == Platform.MOBILE) {
                    MaterialTheme.typography.titleMedium
                } else {
                    MaterialTheme.typography.titleLarge
                },
                color = Color.White,
                fontWeight = FontWeight.Bold
            )

            headerInfo.subtitle?.let { subtitle ->
                Text(
                    text = subtitle,
                    style = if (platform == Platform.MOBILE) {
                        MaterialTheme.typography.bodySmall
                    } else {
                        MaterialTheme.typography.bodyMedium
                    },
                    color = Color.White.copy(alpha = 0.8f)
                )
            }

        }

    }

}
