package com.jycra.filmaico.feature.main.internal

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusProperties
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp

@Composable
fun NavigationItemRow(
    icon: Painter,
    text: String,
    isSelected: Boolean,
    isExpanded: Boolean,
    focusRequester: FocusRequester,
    contentFocusBeacon: FocusRequester?,
    onExpandedChange: (Boolean) -> Unit,
    onClick: () -> Unit
) {

    val contentColor = if (isSelected) MaterialTheme.colorScheme.onSurface
    else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.32f)

    var isFocused by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .background(
                color = if (isFocused) MaterialTheme.colorScheme.surfaceContainerLow.copy(0.64f) else Color.Transparent,
                shape = RoundedCornerShape(8.dp)
            )
            .padding(horizontal = 12.dp)
            .focusRequester(focusRequester)
            .onFocusChanged { focusState ->

                val wasFocused = isFocused
                isFocused = focusState.isFocused || focusState.hasFocus

                if (isFocused) {
                    onExpandedChange(true)
                } else if (wasFocused) {
                    onExpandedChange(false)
                }

            }
            .focusProperties {
                right = contentFocusBeacon ?: FocusRequester.Default
            }
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Box(
            modifier = Modifier.width(48.dp),
            contentAlignment = Alignment.Center
        ) {

            Icon(
                painter = icon,
                contentDescription = null,
                tint = contentColor
            )

        }

        AnimatedVisibility(
            visible = isExpanded,
            enter = fadeIn() + expandHorizontally(),
            exit = fadeOut() + shrinkHorizontally()
        ) {

            Text(
                modifier = Modifier.padding(start = 4.dp),
                text = text,
                style = MaterialTheme.typography.bodyMedium,
                color = contentColor,
                maxLines = 1,
                softWrap = false
            )

        }

    }

}