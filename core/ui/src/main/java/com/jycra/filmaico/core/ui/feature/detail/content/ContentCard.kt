package com.jycra.filmaico.core.ui.feature.detail.content

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.jycra.filmaico.core.navigation.Platform
import com.jycra.filmaico.core.ui.R
import com.jycra.filmaico.core.ui.feature.browse.util.CardDimensions
import com.jycra.filmaico.core.ui.feature.browse.util.CardOrientation
import com.jycra.filmaico.core.ui.feature.browse.util.FocusRestorationHandler
import com.jycra.filmaico.core.ui.feature.browse.util.InitialFocusHandler
import com.jycra.filmaico.core.ui.feature.detail.content.mobile.HorizontalContentCard
import com.jycra.filmaico.core.ui.feature.detail.content.tv.VerticalContentCard
import com.jycra.filmaico.core.ui.util.focus.BrowseFocusCallbacks
import com.jycra.filmaico.core.ui.util.focus.BrowseFocusState

@Composable
fun ContentCard(
    platform: Platform,
    orientation: CardOrientation,
    content: UiContent,
    contentIndex: Int,
    browseFocusState: BrowseFocusState? = null,
    browseFocusCallbacks: BrowseFocusCallbacks? = null,
    onContentClick: (UiContent, Int) -> Unit
) {

    val dimensions = getCardDimensions(platform)

    val focusRequester = if (platform == Platform.TV) {
        remember { FocusRequester() }
    } else null

    if (platform == Platform.TV && focusRequester != null &&
        browseFocusState != null && browseFocusCallbacks != null) {
        InitialFocusHandler(
            focusRequester = focusRequester,
            carouselIndex = 0,
            contentIndex = contentIndex,
            browseFocusState = browseFocusState,
            browseFocusCallbacks = browseFocusCallbacks
        )
        FocusRestorationHandler(
            focusRequester = focusRequester,
            carouselIndex = 0,
            contentIndex = contentIndex,
            browseFocusState = browseFocusState,
            browseFocusCallbacks = browseFocusCallbacks
        )
    }

    val contentType = when (content.type) {
        "episode" -> stringResource(R.string.content_type_episode)
        "movie" -> stringResource(R.string.content_type_movie)
        "ova" -> stringResource(R.string.content_type_ova)
        else -> stringResource(R.string.content_type_episode)
    }

    when (orientation) {
        CardOrientation.HORIZONTAL -> HorizontalContentCard(
            content = content,
            contentType = contentType,
            dimensions = dimensions,
            onContentClick = { onContentClick(content, contentIndex) }
        )
        CardOrientation.VERTICAL -> VerticalContentCard(
            content = content,
            contentType = contentType,
            dimensions = dimensions,
            focusRequester = focusRequester!!,
            onContentClick = { onContentClick(content, contentIndex) }
        )
    }

}

@Composable
private fun getCardDimensions(
    platform: Platform
): CardDimensions {
    return when (platform) {
        Platform.MOBILE -> {
            CardDimensions(
                width = 144.dp,
                height = 81.dp
            )
        }
        Platform.TV -> {
            CardDimensions(
                width = 192.dp,
                height = 108.dp
            )
        }
    }
}