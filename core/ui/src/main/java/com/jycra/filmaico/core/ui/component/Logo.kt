package com.jycra.filmaico.core.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.ui.unit.dp
import com.jycra.filmaico.core.ui.R
import com.jycra.filmaico.core.ui.theme.LocalAppDimens

@Composable
fun FilmaicoLogo(
    modifier: Modifier = Modifier
) {

    val dimens = LocalAppDimens.current

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {

        Icon(
            modifier = Modifier.size(dimens.logoSize),
            tint = MaterialTheme.colorScheme.primary,
            painter = painterResource(R.drawable.ic_filmaico_logo),
            contentDescription = stringResource(R.string.app_logo_content_description)
        )

        Spacer(modifier = Modifier.width(4.dp))

        Text(
            text = stringResource(R.string.app_name),
            style = MaterialTheme.typography.titleMedium
        )

    }

}