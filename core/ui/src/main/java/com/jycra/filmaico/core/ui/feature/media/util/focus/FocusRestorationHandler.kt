package com.jycra.filmaico.core.ui.feature.media.util.focus

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.focus.FocusRequester
import com.jycra.filmaico.core.ui.util.focus.MediaFocusCallbacks
import com.jycra.filmaico.core.ui.util.focus.MediaFocusState
import kotlinx.coroutines.android.awaitFrame

@Composable
fun FocusRestorationHandler(
    focusRequester: FocusRequester,
    carouselIndex: Int,
    contentIndex: Int,
    mediaFocusState: MediaFocusState,
    mediaFocusCallbacks: MediaFocusCallbacks
) {

    val shouldRequestFocus = mediaFocusState.lastFocusedCarouselIndex == carouselIndex &&
            mediaFocusState.lastFocusedContentIndex == contentIndex

    LaunchedEffect(mediaFocusState.shouldRestoreFocus) {
        if (shouldRequestFocus && mediaFocusState.shouldRestoreFocus) {
            awaitFrame()
            focusRequester.requestFocus()
            mediaFocusCallbacks.onFocusRestored()
        }
    }

}