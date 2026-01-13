package com.jycra.filmaico.feature.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jycra.filmaico.core.common.time.formatDaysRemaining
import com.jycra.filmaico.core.ui.component.tab.TabItem
import com.jycra.filmaico.core.ui.theme.color.Gradient
import com.jycra.filmaico.feature.home.HomeUiState
import com.jycra.filmaico.feature.home.common.HomeTabs

@Composable
fun HomeTopHeader(
    uiState: HomeUiState,
    tabs: List<HomeTabs>,
    selectedTabIndex: Int,
    onTabSelected: (Int) -> Unit,
    onProfileClick: () -> Unit
) {

    Column(
        modifier = Modifier
            .background(brush = Gradient.verticalTopBarGradient())
            .statusBarsPadding()
    ) {

        val daysRemaining = when (uiState) {
            is HomeUiState.Success -> formatDaysRemaining(uiState.subscriptionDaysRemaining)
            else -> "--"
        }

        TopBarWithSubscriptionDetail(
            daysRemaining = daysRemaining,
            onProfileClick = onProfileClick
        )

        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp)
        ) {
            itemsIndexed(tabs) { index, tab ->
                TabItem(
                    isSelected = selectedTabIndex == index,
                    onClick = { onTabSelected(index) },
                    text = tab.title
                )
            }
        }

    }

}