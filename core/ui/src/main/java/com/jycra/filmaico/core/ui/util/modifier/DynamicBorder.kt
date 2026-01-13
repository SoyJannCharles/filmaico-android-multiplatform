package com.jycra.filmaico.core.ui.util.modifier

import androidx.compose.animation.core.animate
import androidx.compose.animation.core.tween
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

fun Modifier.dynamicBorder(
    interactionSource: MutableInteractionSource? = null,
    borderColorFocused: Color = Color.White,
    borderColorDefault: Color = Color.Transparent,
    borderWidthFocused: Dp = 3.dp,
    borderWidthDefault: Dp = 0.dp,
    shape: Shape = RoundedCornerShape(12.dp),
    scaleFocused: Float = 1.1f,
    scaleDefault: Float = 1f
): Modifier = composed {

    val isFocused = interactionSource?.collectIsFocusedAsState()?.value ?: false

    var scale by remember { mutableStateOf(scaleDefault) }
    var borderWidth by remember { mutableStateOf(borderWidthDefault) }
    var borderColor by remember { mutableStateOf(borderColorDefault) }

    LaunchedEffect(isFocused) {

        animate(
            initialValue = scale,
            targetValue = if (isFocused) scaleFocused else scaleDefault,
            animationSpec = tween(durationMillis = 128)
        ) { value, _ -> scale = value }

        animate(
            initialValue = borderWidth.value,
            targetValue = if (isFocused) borderWidthFocused.value else borderWidthDefault.value,
            animationSpec = tween(durationMillis = 0)
        ) { value, _ -> borderWidth = value.dp }

        borderColor = if (isFocused) borderColorFocused else borderColorDefault

    }

    this
        .graphicsLayer {
            scaleX = scale
            scaleY = scale
        }
        .border(width = borderWidth, color = borderColor, shape = shape)

}