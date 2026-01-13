package com.jycra.filmaico.core.ui.feature.browse.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.focus.FocusRequester
import com.jycra.filmaico.core.ui.util.focus.BrowseFocusCallbacks
import com.jycra.filmaico.core.ui.util.focus.BrowseFocusState
import kotlinx.coroutines.android.awaitFrame

@Composable
fun InitialFocusHandler(
    focusRequester: FocusRequester,
    carouselIndex: Int,
    contentIndex: Int,
    browseFocusState: BrowseFocusState,
    browseFocusCallbacks: BrowseFocusCallbacks
) {

    val isInitialFocusTarget = carouselIndex == 0 && contentIndex == 0

    LaunchedEffect(isInitialFocusTarget) {
        if (isInitialFocusTarget && !browseFocusState.hasConsumedInitialFocus) {
            awaitFrame()
            focusRequester.requestFocus()
            browseFocusCallbacks.onFocusConsumed()
        }
    }

}