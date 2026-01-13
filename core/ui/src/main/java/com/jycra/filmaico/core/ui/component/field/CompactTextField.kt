package com.jycra.filmaico.core.ui.component.field

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp

@Composable
fun CompactTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String,
    textStyle: TextStyle = MaterialTheme.typography.bodyLarge,
) {

    val customTextSelectionColors = TextSelectionColors(
        handleColor = MaterialTheme.colorScheme.primary,
        backgroundColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.4f)
    )

    SubcomposeLayout(modifier = modifier) { constraints ->
        // 1. Medimos el tamaño de la etiqueta para saber cuánto espacio necesita
        val labelPlaceable = subcompose("label") {
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                modifier = Modifier.padding(horizontal = 4.dp)
            )
        }.first().measure(constraints)

        // 2. Definimos el espacio para el campo de texto interno
        val textFieldInnerPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)

        // 3. Medimos el campo de texto (BasicTextField)
        val textFieldPlaceable = subcompose("textField") {
            CompositionLocalProvider(LocalTextSelectionColors provides customTextSelectionColors) {
                BasicTextField(
                    value = value,
                    onValueChange = onValueChange,
                    modifier = Modifier.padding(textFieldInnerPadding),
                    textStyle = textStyle.copy(color = MaterialTheme.colorScheme.onSurface),
                    singleLine = true,
                    cursorBrush = SolidColor(MaterialTheme.colorScheme.primary)
                )
            }
        }.first().measure(constraints)

        // 4. Calculamos la altura y el ancho total del componente
        val height = textFieldPlaceable.height
        val width = constraints.maxWidth

        // 5. Medimos el borde, que tendrá un "hueco" para la etiqueta
        val borderPlaceable = subcompose("border") {
            Box(
                modifier = Modifier
                    .border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.primary, // Puedes hacerlo dinámico con el foco
                        shape = MaterialTheme.shapes.small
                    )
            ) {
                // El hueco para la etiqueta
                Spacer(
                    modifier = Modifier
                        .width(labelPlaceable.width.toDp())
                        .height(labelPlaceable.height.toDp())
                        .offset(x = 12.dp, y = (-labelPlaceable.height / 2).toDp())
                        .background(MaterialTheme.colorScheme.surfaceContainerLowest)
                )
            }
        }.first().measure(constraints)

        layout(width, height) {
            // 6. Posicionamos todo en la pantalla
            borderPlaceable.placeRelative(0, 0)
            textFieldPlaceable.placeRelative(0, 0)
            // Posicionamos la etiqueta en la parte superior, con un pequeño offset
            labelPlaceable.placeRelative(12, -labelPlaceable.height / 2)
        }
    }

}