package com.jycra.filmaico.feature.main.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.jycra.filmaico.core.ui.theme.color.Gradient
import com.jycra.filmaico.feature.main.common.TvNavItems

@Composable
fun TvSideNavigation(
    modifier: Modifier = Modifier,
    navItems: List<TvNavItems>,
    currentRoute: String?,
    isExpanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    onNavigate: (String) -> Unit,
    focusRequesters: Map<String, FocusRequester>,
    contentFocusBeacon: FocusRequester?
) {

    val animatedWidth by animateDpAsState(
        targetValue = if (isExpanded) 220.dp else 72.dp,
        label = "SideNavWidth"
    )

    val animatedPadding by animateDpAsState(
        targetValue = if (isExpanded) 16.dp else 4.dp,
        label = "SideNavPadding"
    )

    Column(
        modifier = modifier
            .fillMaxHeight()
            .width(animatedWidth)
            .background(brush = Gradient.horizontalSideGradient())
            .padding(vertical = 32.dp, horizontal = animatedPadding),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {

        navItems.forEach { item ->

            val isSelected = currentRoute == item.route
            val contentColor = if (isSelected) MaterialTheme.colorScheme.onSurface
            else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.32f)

            var isFocused by remember { mutableStateOf(false) }

            val itemRequester = focusRequesters[item.route] ?: FocusRequester()

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(44.dp)
                    .background(
                        color = if (isFocused) MaterialTheme.colorScheme.surfaceContainerLow.copy(0.64f) else Color.Transparent,
                        shape = RoundedCornerShape(8.dp)
                    )
                    .padding(horizontal = 12.dp)
                    .focusRequester(itemRequester)
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
                        onClick = {
                            onNavigate(item.route)
                        }
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Icon(
                    modifier = Modifier.requiredSize(24.dp),
                    painter = painterResource(id = item.iconResId),
                    contentDescription = stringResource(id = item.titleResId),
                    tint = contentColor
                )

                AnimatedVisibility(
                    visible = isExpanded,
                    enter = fadeIn() + expandHorizontally(),
                    exit = fadeOut() + shrinkHorizontally()
                ) {
                    Row {
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = stringResource(id = item.titleResId),
                            style = MaterialTheme.typography.bodyMedium,
                            color = contentColor,
                            maxLines = 1,
                            softWrap = false
                        )
                    }
                }

            }

        }

    }

}