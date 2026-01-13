package com.jycra.filmaico.core.ui.feature.detail.content.tv

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jycra.filmaico.core.navigation.Platform
import com.jycra.filmaico.core.ui.feature.browse.util.CardOrientation
import com.jycra.filmaico.core.ui.feature.detail.content.ContentCard
import com.jycra.filmaico.core.ui.feature.detail.content.UiContent
import com.jycra.filmaico.core.ui.util.focus.BrowseFocusCallbacks
import com.jycra.filmaico.core.ui.util.focus.BrowseFocusState
import com.jycra.filmaico.core.ui.util.focus.ContentFocusCallbacks
import com.jycra.filmaico.core.ui.util.focus.ContentFocusState

@Composable
fun TvContentList(
    contentPadding: PaddingValues,
    contentList: List<UiContent>,
    browseFocusState: BrowseFocusState,
    browseFocusCallbacks: BrowseFocusCallbacks,
    onContentClick: (UiContent, Int) -> Unit
) {
    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(horizontal = 32.dp)
    ) {
        itemsIndexed(
            items = contentList,
            key = { _, content -> content.id }
        ) { index, content ->
            ContentCard(
                platform = Platform.TV,
                orientation = CardOrientation.VERTICAL,
                content = content,
                contentIndex = index,
                browseFocusState = browseFocusState,
                browseFocusCallbacks = browseFocusCallbacks,
                onContentClick = onContentClick
            )
        }
    }
}