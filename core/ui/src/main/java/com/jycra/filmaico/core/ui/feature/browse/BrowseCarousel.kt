package com.jycra.filmaico.core.ui.feature.browse

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusProperties
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jycra.filmaico.core.navigation.Platform
import com.jycra.filmaico.core.ui.feature.browse.model.UiBrowseCarousel
import com.jycra.filmaico.core.ui.feature.browse.util.CardOrientation
import com.jycra.filmaico.core.ui.util.focus.BrowseFocusCallbacks
import com.jycra.filmaico.core.ui.util.focus.BrowseFocusState

@Composable
fun BrowseCarousel(
    modifier: Modifier = Modifier,
    platform: Platform,
    contentPadding: PaddingValues = PaddingValues(horizontal = 16.dp),
    orientation: CardOrientation,
    carousel: UiBrowseCarousel,
    carouselIndex: Int = 0,
    browseFocusState: BrowseFocusState? = null,
    browseFocusCallbacks: BrowseFocusCallbacks? = null,
    onContentClick: (String, Int, Int) -> Unit
) {

    val lazyRowState = rememberLazyListState()

    Column {

        val horizontalPadding = when (platform) {
            Platform.MOBILE -> 16.dp
            Platform.TV -> contentPadding.calculateStartPadding(LayoutDirection.Ltr)
        }

        Text(
            modifier = Modifier.padding(horizontal = horizontalPadding),
            text = carousel.title,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(16.dp))

        LazyRow(
            modifier = Modifier.then(
                if (platform == Platform.TV) {
                    Modifier.focusProperties { canFocus = false }
                } else Modifier
            ),
            state = lazyRowState,
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = if (platform == Platform.MOBILE) {
                PaddingValues(horizontal = 16.dp)
            } else {
                contentPadding
            }
        ) {

            itemsIndexed(
                items = carousel.content,
                key = { _, content -> content.id },
                contentType = { _, content -> content.name }
            ) { contentIndex, content ->
                BrowseCard(
                    modifier = modifier,
                    platform = platform,
                    orientation = orientation,
                    imageUrl = content.imageUrl,
                    name = content.name,
                    onContentClick = {
                        onContentClick(content.id, carouselIndex, contentIndex)
                    },
                    browseFocusState = browseFocusState,
                    browseFocusCallbacks = browseFocusCallbacks,
                    carouselIndex = carouselIndex,
                    contentIndex = contentIndex
                )
            }

        }

    }

}