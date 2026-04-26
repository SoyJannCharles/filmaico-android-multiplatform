package com.jycra.filmaico.feature.search.component.topbar

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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.jycra.filmaico.core.device.Platform
import com.jycra.filmaico.core.ui.component.FilmaicoLogo
import com.jycra.filmaico.core.ui.component.field.FilmaicoTextField
import com.jycra.filmaico.core.ui.component.field.SearchTextField
import com.jycra.filmaico.core.ui.theme.color.Gradient
import com.jycra.filmaico.feature.search.SearchUiState

@Composable
fun SearchTopbar(
    uiState: SearchUiState,
    platform: Platform,
    contentPadding: PaddingValues,
    searchFieldFocusRequester: FocusRequester,
    onQueryChange: (query: String) -> Unit,
    onRequestBrowseFocus: () -> Unit,
    searchResults: @Composable (PaddingValues) -> Unit
) {

    when (platform) {
        Platform.MOBILE -> SearchBarMobile(
            uiState = uiState,
            onQueryChange = onQueryChange,
            searchResults = searchResults
        )
        Platform.TV -> SearchBarTv(
            uiState = uiState,
            contentPadding = contentPadding,
            searchFieldFocusRequester = searchFieldFocusRequester,
            onQueryChange = onQueryChange,
            onRequestBrowseFocus = onRequestBrowseFocus,
            searchResults = searchResults
        )
    }

}

@Composable
private fun SearchBarMobile(
    uiState: SearchUiState,
    onQueryChange: (query: String) -> Unit,
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

                FilmaicoTextField(
                    value = uiState.searchQuery,
                    onValueChange = { onQueryChange(it) },
                    label = "Search"
                )

                Spacer(Modifier.height(16.dp))

                val totalResults = uiState.results.sumOf { it.items.size }

                Text(
                    style = MaterialTheme.typography.titleSmall,
                    text = "Resultados: $totalResults"
                )

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
    onQueryChange: (query: String) -> Unit,
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
                            onRequestBrowseFocus()
                            true
                        } else {
                            false
                        }
                    },
                value = uiState.searchQuery,
                onValueChange = { onQueryChange(it) },
                label = "Search"
            )

            val totalResults = uiState.results.sumOf { it.items.size }

            Text(
                style = MaterialTheme.typography.titleSmall,
                text = "Resultados: $totalResults"
            )

        }

    }

}