package com.jycra.filmaico.feature.search.component.topbar

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusProperties
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.jycra.filmaico.core.navigation.Platform
import com.jycra.filmaico.core.ui.component.FilmaicoLogo
import com.jycra.filmaico.core.ui.component.field.FormTextField
import com.jycra.filmaico.core.ui.component.field.SearchTextField
import com.jycra.filmaico.core.ui.theme.color.Gradient
import com.jycra.filmaico.feature.search.SearchUiEvent
import com.jycra.filmaico.feature.search.SearchUiState
import com.jycra.filmaico.feature.search.component.SearchResultCounter

@Composable
fun SearchTopbar(
    uiState: SearchUiState,
    platform: Platform,
    contentPadding: PaddingValues,
    searchFieldFocusRequester: FocusRequester,
    onEvent: (SearchUiEvent) -> Unit,
    onRequestBrowseFocus: () -> Unit,
    searchResults: @Composable (PaddingValues) -> Unit
) {

    when (platform) {
        Platform.MOBILE -> SearchBarMobile(
            uiState = uiState,
            onEvent = onEvent,
            searchResults = searchResults
        )
        Platform.TV -> SearchBarTv(
            uiState = uiState,
            onEvent = onEvent,
            contentPadding = contentPadding,
            searchFieldFocusRequester = searchFieldFocusRequester,
            onRequestBrowseFocus = onRequestBrowseFocus,
            searchResults = searchResults
        )
    }

}

@Composable
private fun SearchBarMobile(
    uiState: SearchUiState,
    onEvent: (SearchUiEvent) -> Unit,
    searchResults: @Composable (PaddingValues) -> Unit
) {
    Scaffold(
        topBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = MaterialTheme.colorScheme.surfaceContainerLowest)
                    .padding(16.dp)
                    .statusBarsPadding()
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = {}
                    )
            ) {

                FilmaicoLogo(modifier = Modifier.fillMaxWidth())

                Spacer(Modifier.height(24.dp))

                FormTextField(
                    value = uiState.searchQuery,
                    onValueChange = { onEvent(SearchUiEvent.OnQueryChange(it)) },
                    label = "Search"
                )

                Spacer(Modifier.height(16.dp))

                SearchResultCounter(uiState.carousels)

            }
        },
        containerColor = MaterialTheme.colorScheme.surfaceContainerLowest
    ) { innerPadding ->
        searchResults(innerPadding)
    }
}

@Composable
private fun SearchBarTv(
    uiState: SearchUiState,
    contentPadding: PaddingValues,
    searchFieldFocusRequester: FocusRequester,
    onEvent: (SearchUiEvent) -> Unit,
    onRequestBrowseFocus: () -> Unit,
    searchResults: @Composable (PaddingValues) -> Unit
) {

    val density = LocalDensity.current
    var topBarHeight by remember { mutableStateOf(0.dp) }

    Box(modifier = Modifier.fillMaxSize()) {

        searchResults(PaddingValues(
            top = topBarHeight + 48.dp,
            bottom = 48.dp
        ))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(brush = Gradient.verticalDetailGradient())
                .padding(
                    top = 24.dp,
                    start = contentPadding.calculateLeftPadding(LayoutDirection.Ltr),
                    end = 24.dp,
                    bottom = 16.dp
                )
                .onGloballyPositioned { coordinates ->
                    val heightInPixels = coordinates.size.height
                    topBarHeight = with(density) {
                        heightInPixels / this.density
                    }.dp
                },
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            SearchTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(searchFieldFocusRequester)
                    .onPreviewKeyEvent { event ->
                        if (event.type == KeyEventType.KeyDown &&
                            event.key == Key.DirectionDown
                        ) {
                            Log.d("SearchViewModel", "Requesting initial focus")
                            onRequestBrowseFocus()
                            true
                        } else {
                            false
                        }
                    },
                value = uiState.searchQuery,
                onValueChange = { onEvent(SearchUiEvent.OnQueryChange(it)) },
                label = "Search"
            )

            SearchResultCounter(uiState.carousels)

        }

    }

}