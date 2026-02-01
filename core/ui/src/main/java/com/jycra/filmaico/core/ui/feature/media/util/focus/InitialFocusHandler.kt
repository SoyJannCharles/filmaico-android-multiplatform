package com.jycra.filmaico.core.ui.feature.media.util.focus

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.focus.FocusRequester
import com.jycra.filmaico.core.ui.util.focus.MediaFocusCallbacks
import com.jycra.filmaico.core.ui.util.focus.MediaFocusState
import kotlinx.coroutines.android.awaitFrame

@Composable
fun InitialFocusHandler(
    focusRequester: FocusRequester,
    carouselIndex: Int,
    contentIndex: Int,
    mediaFocusState: MediaFocusState,
    mediaFocusCallbacks: MediaFocusCallbacks
) {

    val isInitialFocusTarget = carouselIndex == 0 && contentIndex == 0

    LaunchedEffect(isInitialFocusTarget) {
        if (isInitialFocusTarget && !mediaFocusState.hasConsumedInitialFocus) {
            awaitFrame()
            focusRequester.requestFocus()
            mediaFocusCallbacks.onFocusConsumed()
        }
    }

}