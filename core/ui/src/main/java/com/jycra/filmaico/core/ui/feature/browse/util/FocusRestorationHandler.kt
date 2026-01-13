package com.jycra.filmaico.core.ui.feature.browse.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.focus.FocusRequester
import com.jycra.filmaico.core.ui.util.focus.BrowseFocusCallbacks
import com.jycra.filmaico.core.ui.util.focus.BrowseFocusState
import kotlinx.coroutines.android.awaitFrame

@Composable
fun FocusRestorationHandler(
    focusRequester: FocusRequester,
    carouselIndex: Int,
    contentIndex: Int,
    browseFocusState: BrowseFocusState,
    browseFocusCallbacks: BrowseFocusCallbacks
) {

    val shouldRequestFocus = browseFocusState.lastFocusedCarouselIndex == carouselIndex &&
            browseFocusState.lastFocusedContentIndex == contentIndex

    LaunchedEffect(browseFocusState.shouldRestoreFocus) {
        if (shouldRequestFocus && browseFocusState.shouldRestoreFocus) {
            awaitFrame()
            focusRequester.requestFocus()
            browseFocusCallbacks.onFocusRestored()
        }
    }

}