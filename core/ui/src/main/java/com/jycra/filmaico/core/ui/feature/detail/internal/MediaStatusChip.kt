package com.jycra.filmaico.core.ui.feature.detail.internal

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.jycra.filmaico.core.ui.R
import com.jycra.filmaico.domain.common.content.model.ContentStatus

@Composable
fun MediaStatusChip(
    status: ContentStatus
) {

    val (text, color) = when (status) {
        ContentStatus.AIRING ->
            stringResource(R.string.content_status_airing) to Color(0xFF1DE986)
        ContentStatus.FINISHED ->
            stringResource(R.string.content_status_finished) to Color(0xFFE91D21)
        ContentStatus.ON_HIATUS ->
            stringResource(R.string.content_status_on_hiatus) to Color(0xFFE9A21D)
        ContentStatus.UNKNOWN ->
            stringResource(R.string.content_status_unknown) to MaterialTheme.colorScheme.onSurface.copy(0.64f)
    }

    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {

        Box(
            modifier = Modifier
                .size(8.dp)
                .background(color, CircleShape)
        )

        Spacer(modifier = Modifier.width(4.dp))

        Text(
            style = MaterialTheme.typography.labelLarge,
            color = color,
            text = text,
        )

    }

}