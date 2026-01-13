package com.jycra.filmaico.core.ui.component.dialog

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.jycra.filmaico.core.ui.R

@Composable
fun ConfirmationDialog(
    title: String = stringResource(R.string.confirmation_dialog_title),
    message: String = stringResource(R.string.confirmation_dialog_message),
    confirmText: String = stringResource(R.string.confirmation_dialog_confirm_button),
    cancelText: String = stringResource(R.string.confirmation_dialog_cancel_button),
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    isDestructive: Boolean = false
) {

    BaseDialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(dismissOnBackPress = true, dismissOnClickOutside = true),
        icon = {
            Icon(
                painter = painterResource(R.drawable.ic_warning),
                contentDescription = null,
                tint = if (isDestructive) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface
            )
        },
        title = {
            Text(text = title, textAlign = TextAlign.Center)
        },
        text = {
            Text(text = message, textAlign = TextAlign.Center)
        },
        actions = {
            TextButton(onClick = onDismiss) {
                Text(text = cancelText)
            }
            Spacer(modifier = Modifier.width(8.dp))
            Button(
                onClick = onConfirm,
                colors = if (isDestructive) ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error) else ButtonDefaults.buttonColors()
            ) {
                Text(text = confirmText)
            }
        }
    )

}