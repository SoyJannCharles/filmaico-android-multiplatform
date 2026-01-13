package com.jycra.filmaico.core.ui.component.field

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.VisualTransformation

@Composable
fun FormTextField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    isError: Boolean = false,
    visualTransformation: VisualTransformation = VisualTransformation.None
) {

    OutlinedTextField(
        modifier = modifier
            .fillMaxWidth(),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            unfocusedBorderColor = MaterialTheme.colorScheme.surfaceBright,
            disabledBorderColor = MaterialTheme.colorScheme.surfaceBright,
            focusedTextColor = MaterialTheme.colorScheme.onSurface,
            unfocusedTextColor = MaterialTheme.colorScheme.surfaceBright,
            disabledTextColor = MaterialTheme.colorScheme.surfaceBright,
            focusedLabelColor = MaterialTheme.colorScheme.primary,
            unfocusedLabelColor = MaterialTheme.colorScheme.surfaceBright,
            disabledLabelColor = MaterialTheme.colorScheme.surfaceBright,
        ),
        visualTransformation = visualTransformation,
        isError = isError,
        singleLine = true,
        label = { Text(text =label) },
        value = value,
        onValueChange = onValueChange
    )

}