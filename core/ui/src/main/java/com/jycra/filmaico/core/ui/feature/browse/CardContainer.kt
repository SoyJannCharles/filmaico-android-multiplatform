package com.jycra.filmaico.core.ui.feature.browse

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEvent
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.unit.dp
import com.jycra.filmaico.core.navigation.Platform
import com.jycra.filmaico.core.ui.component.card.OptimizedCard
import com.jycra.filmaico.core.ui.feature.browse.util.CardDimensions
import com.jycra.filmaico.core.ui.util.focus.BrowseFocusCallbacks

@Composable
fun CardContainer(
    platform: Platform,
    dimensions: CardDimensions,
    focusRequester: FocusRequester?,
    carouselIndex: Int,
    contentIndex: Int,
    browseFocusCallbacks: BrowseFocusCallbacks?,
    onClick: () -> Unit,
    content: @Composable () -> Unit
) {

    when (platform) {

        Platform.MOBILE -> {

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(dimensions.height),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.Transparent),
                onClick = onClick
            ) {
                content()
            }

        }

        Platform.TV -> {

            val isSelected = remember { mutableStateOf(false) }

            OptimizedCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(dimensions.height)
                    .then(
                        if (focusRequester != null) {
                            Modifier.focusRequester(focusRequester)
                        } else Modifier
                    )
                    .then(
                        if (browseFocusCallbacks != null) {
                            Modifier.onKeyEvent { keyEvent ->
                                handleLeftKeyEvent(
                                    keyEvent = keyEvent,
                                    carouselIndex = carouselIndex,
                                    contentIndex = contentIndex,
                                    browseFocusCallbacks = browseFocusCallbacks
                                )
                            }
                        } else Modifier
                    ),
                selected = isSelected,
                onClick = onClick
            ) {
                content()
            }

        }

    }

}

private fun handleLeftKeyEvent(
    keyEvent: KeyEvent,
    carouselIndex: Int,
    contentIndex: Int,
    browseFocusCallbacks: BrowseFocusCallbacks
): Boolean {
    if (keyEvent.type == KeyEventType.KeyDown && keyEvent.key == Key.DirectionLeft) {
        if (contentIndex == 0) {
            browseFocusCallbacks.onFocusLeft(carouselIndex, contentIndex)
            return true
        }
    }
    return false
}