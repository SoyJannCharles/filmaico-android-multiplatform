package com.jycra.filmaico.core.ui.feature.detail.content

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import com.jycra.filmaico.core.navigation.Platform
import com.jycra.filmaico.core.ui.feature.detail.content.mobile.MobileContentList
import com.jycra.filmaico.core.ui.feature.detail.content.tv.TvContentList
import com.jycra.filmaico.core.ui.util.focus.BrowseFocusCallbacks
import com.jycra.filmaico.core.ui.util.focus.BrowseFocusState

@Composable
fun ContentList(
    platform: Platform,
    contentPadding: PaddingValues = PaddingValues(),
    contentList: List<UiContent>,
    browseFocusState: BrowseFocusState? = null,
    browseFocusCallbacks: BrowseFocusCallbacks? = null,
    onContentClick: (UiContent, Int) -> Unit
) {
    when (platform) {
        Platform.MOBILE -> MobileContentList(
            contentPadding = contentPadding,
            contentList = contentList,
            onContentClick = onContentClick
        )
        Platform.TV -> TvContentList(
            contentPadding = contentPadding,
            contentList = contentList,
            browseFocusState = browseFocusState!!,
            browseFocusCallbacks = browseFocusCallbacks!!,
            onContentClick = onContentClick
        )
    }
}