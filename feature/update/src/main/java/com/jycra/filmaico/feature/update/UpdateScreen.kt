package com.jycra.filmaico.feature.update

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.jycra.filmaico.core.ui.R
import com.jycra.filmaico.core.ui.component.FilmaicoLogo
import com.jycra.filmaico.core.ui.component.common.InfoGroup
import com.jycra.filmaico.core.ui.component.common.InfoRow

@Composable
fun UpdateScreen(
    currentVersion: String,
    serverVersion: String
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.surfaceContainerLowest)
            .padding(all = 24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        FilmaicoLogo(modifier = Modifier.fillMaxWidth())

        Column(
            modifier = Modifier
                .weight(1f),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                style = MaterialTheme.typography.titleLarge,
                text = stringResource(R.string.update_required_title),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.64f),
                text = stringResource(R.string.update_required_body),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(64.dp))

            InfoGroup {

                InfoRow(
                    label = stringResource(R.string.update_required_version_current_label),
                    value = currentVersion
                )

                HorizontalDivider(color = MaterialTheme.colorScheme.surfaceBright)

                InfoRow(
                    label = stringResource(R.string.update_required_version_latest_label),
                    value = serverVersion
                )

            }

        }

    }

}