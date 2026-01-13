package com.jycra.filmaico.core.ui.component.dialog

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.window.DialogProperties
import com.jycra.filmaico.core.ui.R

@Composable
fun ActionableDialog(
    onDismissRequest: () -> Unit,
    onConfirm: () -> Unit,
    title: String,
    text: String,
    confirmButtonText: String = stringResource(R.string.actionable_dialog_button),
) {

    BaseDialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true
        ),
        icon = { Icon(Icons.Default.Warning, contentDescription = null) },
        title = { Text(
            text = title,
            textAlign = TextAlign.Center
        ) },
        text = { Text(
            text = text,
            textAlign = TextAlign.Center
        ) },
        actions = {
            Button(onClick = onConfirm) {
                Text(confirmButtonText)
            }
        }
    )

}