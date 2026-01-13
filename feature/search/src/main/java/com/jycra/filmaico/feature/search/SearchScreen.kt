package com.jycra.filmaico.feature.search

import android.util.Log
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.focus.FocusRequester
import com.jycra.filmaico.core.navigation.Platform
import com.jycra.filmaico.core.ui.util.focus.BrowseFocusCallbacks
import com.jycra.filmaico.core.ui.util.focus.BrowseFocusState
import com.jycra.filmaico.core.ui.util.focus.FocusBeacon
import com.jycra.filmaico.feature.search.component.topbar.SearchResults
import com.jycra.filmaico.feature.search.component.topbar.SearchTopbar

@Composable
fun SearchScreen(
    platform: Platform,
    uiState: SearchUiState,
    contentPadding: PaddingValues,
    onRequestBrowseFocus: () -> Unit,
    browseFocusState: BrowseFocusState,
    browseFocusCallbacks: BrowseFocusCallbacks,
    searchFieldFocusRequester: FocusRequester,
    contentFocusBeacon: FocusRequester? = null,
    onEvent: (SearchUiEvent) -> Unit
) {

    Screen(
        platform = platform,
        uiState = uiState,
        contentPadding = contentPadding,
        onRequestBrowseFocus = onRequestBrowseFocus,
        browseFocusState = browseFocusState,
        browseFocusCallbacks = browseFocusCallbacks,
        searchFieldFocusRequester = searchFieldFocusRequester,
        contentFocusBeacon = contentFocusBeacon,
        onEvent = onEvent
    )

}

@Composable
private fun Screen(
    platform: Platform,
    uiState: SearchUiState,
    contentPadding: PaddingValues,
    onRequestBrowseFocus: () -> Unit,
    browseFocusState: BrowseFocusState? = null,
    browseFocusCallbacks: BrowseFocusCallbacks? = null,
    searchFieldFocusRequester: FocusRequester,
    contentFocusBeacon: FocusRequester? = null,
    onEvent: (SearchUiEvent) -> Unit
) {

    SearchTopbar(
        uiState = uiState,
        platform = platform,
        contentPadding = contentPadding,
        onEvent = onEvent,
        onRequestBrowseFocus = onRequestBrowseFocus,
        searchFieldFocusRequester = searchFieldFocusRequester
    ) { innerPadding ->
        SearchResults(
            uiState = uiState,
            onEvent = onEvent,
            platform = platform,
            contentPadding = contentPadding,
            innerPadding = innerPadding,
            browseFocusState = browseFocusState,
            browseFocusCallbacks = browseFocusCallbacks,
            searchFieldFocusRequester = searchFieldFocusRequester
        )
    }

    if (platform == Platform.TV && contentFocusBeacon != null &&
        browseFocusState != null && browseFocusCallbacks != null
    ) {
        FocusBeacon(
            focusRequester = contentFocusBeacon,
            browseFocusCallbacks = browseFocusCallbacks
        )
    }

}