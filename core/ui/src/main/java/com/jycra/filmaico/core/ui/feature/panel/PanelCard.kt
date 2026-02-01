package com.jycra.filmaico.core.ui.feature.panel

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.unit.dp
import com.jycra.filmaico.core.ui.component.card.OptimizedCard
import com.jycra.filmaico.core.ui.feature.media.util.focus.FocusRestorationHandler
import com.jycra.filmaico.core.ui.feature.media.util.focus.InitialFocusHandler
import com.jycra.filmaico.core.ui.feature.media.util.handleLeftKeyEvent
import com.jycra.filmaico.core.ui.util.focus.MediaFocusCallbacks
import com.jycra.filmaico.core.ui.util.focus.MediaFocusState

@Composable
fun PanelCard(
    modifier: Modifier = Modifier,
    title: String,
    subtitle: String,
    carouselIndex: Int = 0,
    contentIndex: Int = 0,
    mediaFocusState: MediaFocusState? = null,
    mediaFocusCallbacks: MediaFocusCallbacks? = null,
    onContentClick: () -> Unit
) {

    val isSelected = remember { mutableStateOf(false) }

    val focusRequester = remember { FocusRequester() }

    if (mediaFocusState != null && mediaFocusCallbacks != null) {
        InitialFocusHandler(focusRequester, carouselIndex, contentIndex, mediaFocusState, mediaFocusCallbacks)
        FocusRestorationHandler(focusRequester, carouselIndex, contentIndex, mediaFocusState, mediaFocusCallbacks)
    }

    OptimizedCard(
        modifier = modifier
            .fillMaxWidth()
            .focusRequester(focusRequester)
            .then(
                if (mediaFocusCallbacks != null) {
                    Modifier.onKeyEvent { keyEvent ->
                        handleLeftKeyEvent(
                            keyEvent = keyEvent,
                            carouselIndex = carouselIndex,
                            contentIndex = contentIndex,
                            mediaFocusCallbacks = mediaFocusCallbacks
                        )
                    }
                } else Modifier
            ),
        selected = isSelected,
        onClick = onContentClick
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center
        ) {

            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.32f),
                maxLines = 2
            )

        }

    }

}