package com.jycra.filmaico.feature.main.internal

import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.jycra.filmaico.core.ui.R
import com.jycra.filmaico.core.ui.component.FilmaicoLogoSidebar
import com.jycra.filmaico.core.ui.theme.color.Gradient
import com.jycra.filmaico.feature.main.common.NavItems

@SuppressLint("RememberInComposition")
@Composable
fun Sidebar(
    modifier: Modifier = Modifier,
    mainNavItems: List<NavItems>,
    submenuNavItems: List<NavItems>,
    expirationText: String,
    currentRoute: String?,
    isExpanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    onNavigate: (String) -> Unit,
    focusRequesters: Map<String, FocusRequester>,
    contentFocusBeacon: FocusRequester?
) {

    var isShowingSubMenu by remember { mutableStateOf(false) }
    var wasInSubmenu by remember { mutableStateOf(false) }

    LaunchedEffect(isExpanded) {
        if (!isExpanded && isShowingSubMenu) {
            isShowingSubMenu = false
        }
    }

    LaunchedEffect(isShowingSubMenu) {
        if (isShowingSubMenu) {
            wasInSubmenu = true
        } else if (wasInSubmenu) {
            try {
                focusRequesters[NavItems.MORE.route]?.requestFocus()
            } catch (e: Exception) {

            }
            wasInSubmenu = false
        }
    }

    BackHandler(enabled = isShowingSubMenu) {
        isShowingSubMenu = false
    }

    val backButtonRequester = remember { FocusRequester() }

    val animatedWidth by animateDpAsState(
        targetValue = if (isExpanded) 220.dp else 72.dp,
        label = "SideNavWidth"
    )

    val animatedPadding by animateDpAsState(
        targetValue = if (isExpanded) 16.dp else 4.dp,
        label = "SideNavPadding"
    )

    val isCurrentRouteInSubmenu = submenuNavItems.any { it.route == currentRoute }

    Column(
        modifier = modifier
            .fillMaxHeight()
            .width(animatedWidth)
            .background(brush = Gradient.horizontalSideGradient())
            .padding(vertical = 32.dp, horizontal = animatedPadding),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {

        FilmaicoLogoSidebar(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp),
            isExpanded = isExpanded
        )

        Spacer(modifier = Modifier.height(12.dp))

        AnimatedContent(
            targetState = isShowingSubMenu,
            transitionSpec = {
                if (targetState) {
                    slideInHorizontally { it } + fadeIn() togetherWith
                            slideOutHorizontally { -it } + fadeOut()
                } else {
                    slideInHorizontally { -it } + fadeIn() togetherWith
                            slideOutHorizontally { it } + fadeOut()
                }
            },
            label = "MenuTransition"
        ) { subMenuVisible ->

            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {

                if (subMenuVisible) {

                    NavigationItemRow(
                        icon = painterResource(R.drawable.ic_arrow_back),
                        text = stringResource(R.string.sidebar_item_return),
                        isSelected = false,
                        isExpanded = isExpanded,
                        focusRequester = backButtonRequester,
                        contentFocusBeacon = contentFocusBeacon,
                        onExpandedChange = onExpandedChange,
                        onClick = { isShowingSubMenu = false }
                    )

                    submenuNavItems.forEach { item ->

                        NavigationItemRow(
                            icon = painterResource(item.iconResId),
                            text = stringResource(item.titleResId),
                            isSelected = currentRoute == item.route,
                            isExpanded = isExpanded,
                            focusRequester = focusRequesters[item.route] ?: FocusRequester(),
                            contentFocusBeacon = contentFocusBeacon,
                            onExpandedChange = onExpandedChange,
                            onClick = {
                                onNavigate(item.route)
                                isShowingSubMenu = false
                            }
                        )

                    }

                } else {

                    mainNavItems.forEach { item ->

                        val isItemSelected = if (item == NavItems.MORE) {
                            currentRoute == item.route || isCurrentRouteInSubmenu
                        } else {
                            currentRoute == item.route
                        }

                        NavigationItemRow(
                            icon = painterResource(item.iconResId),
                            text = stringResource(item.titleResId),
                            isSelected = isItemSelected,
                            isExpanded = isExpanded,
                            focusRequester = focusRequesters[item.route] ?: FocusRequester(),
                            contentFocusBeacon = contentFocusBeacon,
                            onExpandedChange = onExpandedChange,
                            onClick = {
                                if (item == NavItems.MORE) {
                                    isShowingSubMenu = true
                                } else {
                                    onNavigate(item.route)
                                }
                            }
                        )

                    }

                }

            }

        }

        Spacer(modifier = Modifier.weight(1f))

        SidebarSubscriptionCard(
            text = expirationText,
            isExpanded = isExpanded
        )

        val profileItem = NavItems.PROFILE
        NavigationItemRow(
            icon = painterResource(profileItem.iconResId),
            text = stringResource(profileItem.titleResId),
            isSelected = currentRoute == profileItem.route,
            isExpanded = isExpanded,
            focusRequester = focusRequesters[profileItem.route] ?: FocusRequester(),
            contentFocusBeacon = contentFocusBeacon,
            onExpandedChange = onExpandedChange,
            onClick = { onNavigate(profileItem.route) }
        )

    }

}