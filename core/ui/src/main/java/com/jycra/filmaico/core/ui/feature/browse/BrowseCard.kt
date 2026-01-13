package com.jycra.filmaico.core.ui.feature.browse

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.jycra.filmaico.core.navigation.Platform
import com.jycra.filmaico.core.ui.feature.browse.util.CardDimensions
import com.jycra.filmaico.core.ui.feature.browse.util.CardOrientation
import com.jycra.filmaico.core.ui.feature.browse.util.FocusRestorationHandler
import com.jycra.filmaico.core.ui.feature.browse.util.InitialFocusHandler
import com.jycra.filmaico.core.ui.util.focus.BrowseFocusCallbacks
import com.jycra.filmaico.core.ui.util.focus.BrowseFocusState

@Composable
fun BrowseCard(
    modifier: Modifier = Modifier,
    platform: Platform,
    orientation: CardOrientation,
    imageUrl: String,
    name: String,
    carouselIndex: Int = 0,
    contentIndex: Int = 0,
    browseFocusState: BrowseFocusState? = null,
    browseFocusCallbacks: BrowseFocusCallbacks? = null,
    onContentClick: () -> Unit
) {

    val dimensions = getCardDimensions(platform, orientation)

    val focusRequester = if (platform == Platform.TV) {
        remember { FocusRequester() }
    } else null

    if (platform == Platform.TV && focusRequester != null &&
        browseFocusState != null && browseFocusCallbacks != null) {

        InitialFocusHandler(
            focusRequester = focusRequester,
            carouselIndex = carouselIndex,
            contentIndex = contentIndex,
            browseFocusState = browseFocusState,
            browseFocusCallbacks = browseFocusCallbacks
        )

        FocusRestorationHandler(
            focusRequester = focusRequester,
            carouselIndex = carouselIndex,
            contentIndex = contentIndex,
            browseFocusState = browseFocusState,
            browseFocusCallbacks = browseFocusCallbacks
        )

    }

    Column(
        modifier = modifier.width(dimensions.width),
        horizontalAlignment = Alignment.Start
    ) {

        CardContainer(
            platform = platform,
            dimensions = dimensions,
            focusRequester = focusRequester,
            carouselIndex = carouselIndex,
            contentIndex = contentIndex,
            browseFocusCallbacks = browseFocusCallbacks,
            onClick = onContentClick
        ) {
            CardContent(
                orientation = orientation,
                imageUrl = imageUrl,
                name = name,
                dimensions = dimensions
            )
        }

        Text(
            modifier = Modifier
                .padding(top = 8.dp, start = 4.dp, end = 4.dp),
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.onSurface,
            text = name,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

    }

}

@Composable
private fun getCardDimensions(
    platform: Platform,
    orientation: CardOrientation
): CardDimensions {
    return when (platform) {
        Platform.MOBILE -> when (orientation) {
            CardOrientation.VERTICAL -> CardDimensions(
                width = 128.dp,
                height = 192.dp
            )
            CardOrientation.HORIZONTAL -> CardDimensions(
                width = 192.dp,
                height = 128.dp
            )
        }
        Platform.TV -> when (orientation) {
            CardOrientation.VERTICAL -> CardDimensions(
                width = 128.dp,
                height = 192.dp
            )
            CardOrientation.HORIZONTAL -> CardDimensions(
                width = 192.dp,
                height = 128.dp
            )
        }
    }
}