package com.jycra.filmaico.core.ui.component.dialog

import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.window.DialogProperties
import com.jycra.filmaico.core.ui.R

@Composable
fun ErrorDialog(
    title: String = stringResource(R.string.error_dialog_title),
    message: String = stringResource(R.string.error_dialog_message),
    buttonText: String = stringResource(R.string.error_dialog_button),
    onDismiss: () -> Unit
) {

    BaseDialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(dismissOnBackPress = true, dismissOnClickOutside = true),
        icon = {
            Icon(
                painter = painterResource(R.drawable.ic_error),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.error
            )
        },
        title = {
            Text(text = title, textAlign = TextAlign.Center)
        },
        text = {
            Text(text = message, textAlign = TextAlign.Center)
        },
        actions = {
            Button(onClick = onDismiss) {
                Text(text = buttonText)
            }
        }
    )

}