package com.jycra.filmaico.core.ui.util.focus

import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.unit.dp

@Composable
fun FocusBeacon(
    focusRequester: FocusRequester,
    mediaFocusCallbacks: MediaFocusCallbacks
) {

    Box(
        modifier = Modifier
            .size(0.dp)
            .focusRequester(focusRequester)
            .onFocusChanged { focusState ->
                if (focusState.isFocused) {
                    mediaFocusCallbacks.onBeaconReceived()
                }
            }
            .focusable()
    )

}