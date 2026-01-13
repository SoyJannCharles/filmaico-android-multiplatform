package com.jycra.filmaico.core.ui.feature.detail.navigation

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.jycra.filmaico.core.ui.R
import com.jycra.filmaico.core.ui.component.FilmaicoLogo

@Composable
fun DetailTopBar(
    onBackPressed: () -> Unit
) {

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

        FilmaicoLogo(
            modifier = Modifier
                .weight(1f)
        )

        Spacer(modifier = Modifier.width(48.dp))

    }

}