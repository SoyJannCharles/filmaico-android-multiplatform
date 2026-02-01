package com.jycra.filmaico.core.ui.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.jycra.filmaico.core.ui.R

@Composable
fun FilmaicoLogo(
    modifier: Modifier = Modifier
) {

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {

        Icon(
            modifier = Modifier.size(20.dp),
            painter = painterResource(R.drawable.ic_filmaico_logo),
            tint = MaterialTheme.colorScheme.primary,
            contentDescription = stringResource(R.string.app_logo_content_description)
        )

        Text(
            modifier = Modifier.padding(start = 8.dp),
            text = stringResource(R.string.app_name),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface,
            maxLines = 1,
            softWrap = false
        )

    }

}

@Composable
fun FilmaicoLogoSidebar(
    modifier: Modifier = Modifier,
    isExpanded: Boolean = true
) {

    Row(
        modifier = modifier.height(48.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {

        Box(
            modifier = Modifier.width(48.dp),
            contentAlignment = Alignment.Center
        ) {

            Icon(
                tint = MaterialTheme.colorScheme.primary,
                painter = painterResource(R.drawable.ic_filmaico_logo),
                contentDescription = stringResource(R.string.app_logo_content_description)
            )

        }

        AnimatedVisibility(
            visible = isExpanded,
            enter = fadeIn() + expandHorizontally(),
            exit = fadeOut() + shrinkHorizontally()
        ) {

            Text(
                modifier = Modifier.padding(start = 4.dp),
                text = stringResource(R.string.app_name),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                softWrap = false
            )

        }

    }

}