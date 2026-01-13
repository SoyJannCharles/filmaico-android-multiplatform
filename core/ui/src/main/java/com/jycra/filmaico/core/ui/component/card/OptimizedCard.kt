package com.jycra.filmaico.core.ui.component.card

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.jycra.filmaico.core.ui.util.modifier.dynamicBorder

@Composable
fun OptimizedCard(
    modifier: Modifier = Modifier,
    selected: MutableState<Boolean>,
    onClick: () -> Unit,
    content: @Composable () -> Unit
) {

    val interactionSource = remember { MutableInteractionSource() }

    Surface(
        modifier = modifier
            .selectable(
                selected = selected.value,
                onClick = {
                    selected.value = !selected.value
                    onClick()
                },
                interactionSource = interactionSource,
                indication = null
            )
            .dynamicBorder(interactionSource = interactionSource),
        shape = RoundedCornerShape(12.dp),
        color = Color.Transparent
    ) {
        content()
    }

}