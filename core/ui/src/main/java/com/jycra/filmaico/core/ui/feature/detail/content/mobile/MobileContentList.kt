package com.jycra.filmaico.core.ui.feature.detail.content.mobile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jycra.filmaico.core.navigation.Platform
import com.jycra.filmaico.core.ui.feature.browse.util.CardOrientation
import com.jycra.filmaico.core.ui.feature.detail.content.ContentCard
import com.jycra.filmaico.core.ui.feature.detail.content.UiContent

@Composable
fun MobileContentList(
    contentPadding: PaddingValues,
    contentList: List<UiContent>,
    onContentClick: (UiContent, Int) -> Unit
) {

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = contentPadding
    ) {

        itemsIndexed(
            items = contentList,
            key = { _, content -> content.id }
        ) { index, content ->

            ContentCard(
                platform = Platform.MOBILE,
                orientation = CardOrientation.HORIZONTAL,
                content = content,
                contentIndex = index,
                onContentClick = onContentClick
            )

        }

    }

}