package com.jycra.filmaico.core.ui.component.dialog

import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.window.DialogProperties
import com.jycra.filmaico.core.ui.R

@Composable
fun NoInternetDialog() {

    BaseDialog(
        onDismissRequest = {},
        properties = DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false),
        icon = {
            Icon(
                painter = painterResource(R.drawable.ic_no_internet),
                contentDescription = null
            )
        },
        title = {
            Text(
                text = stringResource(R.string.no_internet_dialog_title),
                textAlign = TextAlign.Center
            )
        },
        text = {
            Text(
                text = stringResource(R.string.no_internet_dialog_body),
                textAlign = TextAlign.Center
            )
        },
        hasActions = false,
        actions = {}
    )

}