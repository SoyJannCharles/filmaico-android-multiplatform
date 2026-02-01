package com.jycra.filmaico.feature.panel

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.jycra.filmaico.core.device.Platform
import com.jycra.filmaico.core.ui.R
import com.jycra.filmaico.core.ui.feature.panel.PanelCard
import com.jycra.filmaico.core.ui.feature.panel.model.PanelSection
import com.jycra.filmaico.core.ui.feature.panel.model.UiPanel
import com.jycra.filmaico.core.ui.theme.color.Gradient
import com.jycra.filmaico.core.ui.util.focus.FocusBeacon
import com.jycra.filmaico.core.ui.util.focus.MediaFocusCallbacks
import com.jycra.filmaico.core.ui.util.focus.MediaFocusState
import com.jycra.filmaico.feature.panel.internal.DevicesInfoContent
import com.jycra.filmaico.feature.panel.internal.ProfileInfoContent

@Composable
fun AccountScreen(
    uiState: AccountUiState,
    platform: Platform,
    contentPadding: PaddingValues,
    mediaFocusState: MediaFocusState,
    mediaFocusCallbacks: MediaFocusCallbacks,
    contentFocusBeacon: FocusRequester? = null,
    onEvent: (PanelUiEvent) -> Unit
) {

    when (uiState) {
        is AccountUiState.Loading -> {

        }
        is AccountUiState.Success -> {
            Screen(
                platform = platform,
                uiPanel = uiState.uiPanel,
                contentPadding = contentPadding,
                mediaFocusState = mediaFocusState,
                mediaFocusCallbacks = mediaFocusCallbacks,
                contentFocusBeacon = contentFocusBeacon,
                selectedSection = uiState.selectedSection,
                onSectionSelected = { sectionSelected ->
                    onEvent(PanelUiEvent.SectionSelected(sectionSelected))
                },
                onSignOut = { onEvent(PanelUiEvent.SignOut) }
            )
        }
        is AccountUiState.Error -> {

        }
        is AccountUiState.Unauthenticated -> {
            onEvent(PanelUiEvent.SignOut)
        }
    }

}

@Composable
private fun Screen(
    platform: Platform,
    uiPanel: UiPanel,
    contentPadding: PaddingValues,
    mediaFocusState: MediaFocusState? = null,
    mediaFocusCallbacks: MediaFocusCallbacks? = null,
    contentFocusBeacon: FocusRequester? = null,
    selectedSection: PanelSection,
    onSectionSelected: (PanelSection) -> Unit,
    onSignOut: () -> Unit
) {

    when (platform) {

        Platform.MOBILE -> {

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(32.dp),
                contentPadding = PaddingValues(
                    top = 128.dp,
                    bottom = contentPadding.calculateBottomPadding() + 32.dp,
                    start = 16.dp,
                    end = 16.dp
                )
            ) {

                item {
                    Text(
                        text = stringResource(R.string.account_title),
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold
                    )
                }

                item {
                    SectionWrapper(title = stringResource(R.string.account_profile_settings_title)) {
                        ProfileInfoContent(uiPanel = uiPanel)
                    }
                }

                item {
                    SectionWrapper(title = stringResource(R.string.account_devices_settings_title)) {
                        DevicesInfoContent(uiPanel = uiPanel)
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = { onSignOut() },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.errorContainer,
                            contentColor = MaterialTheme.colorScheme.onErrorContainer
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(text = stringResource(R.string.account_signout_title), fontWeight = FontWeight.SemiBold)
                    }
                }

            }

        }
        Platform.TV -> {

            val topBarHeight = 80.dp
            val leftPadding = contentPadding.calculateLeftPadding(LocalLayoutDirection.current)

            Box(modifier = Modifier.fillMaxSize()) {

                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(start = leftPadding, end = 32.dp),
                    horizontalArrangement = Arrangement.spacedBy(48.dp)
                ) {

                    Column(
                        modifier = Modifier.weight(0.35f)
                    ) {

                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                            contentPadding = PaddingValues(
                                top = topBarHeight + 16.dp,
                                bottom = 32.dp
                            )
                        ) {

                            itemsIndexed(
                                items = uiPanel.panelOptions,
                                key = { _, panel -> panel.section }
                            ) { carouselIndex, panel ->

                                PanelCard(
                                    title = panel.title,
                                    subtitle = panel.subtitle,
                                    carouselIndex = carouselIndex,
                                    mediaFocusState = mediaFocusState,
                                    mediaFocusCallbacks = mediaFocusCallbacks,
                                    onContentClick = {
                                        if (panel.section == PanelSection.LOGOUT) onSignOut()
                                        else onSectionSelected(panel.section)
                                    }
                                )

                            }

                        }

                    }

                    Column(
                        modifier = Modifier
                            .weight(0.65f)
                            .padding(top = topBarHeight + 16.dp)
                    ) {

                        AnimatedContent(
                            targetState = selectedSection,
                            label = "AccountContentTransition"
                        ) { section ->
                            when (section) {
                                PanelSection.MY_PROFILE -> ProfileInfoContent(
                                    modifier = Modifier.fillMaxSize(),
                                    uiPanel = uiPanel
                                )
                                PanelSection.MY_DEVICES -> DevicesInfoContent(
                                    modifier = Modifier.fillMaxSize(),
                                    uiPanel = uiPanel
                                )
                                else -> Unit
                            }
                        }

                    }

                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(topBarHeight)
                        .background(brush = Gradient.verticalDetailGradient())
                        .padding(start = leftPadding, top = 32.dp)
                ) {

                    Text(
                        text = stringResource(R.string.account_title),
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                }

            }

        }

    }

    if (platform == Platform.TV && contentFocusBeacon != null &&
        mediaFocusState != null && mediaFocusCallbacks != null) {
        FocusBeacon(
            focusRequester = contentFocusBeacon,
            mediaFocusCallbacks = mediaFocusCallbacks
        )
    }

}

@Composable
fun SectionWrapper(
    title: String,
    content: @Composable () -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.SemiBold
        )
        content()
    }
}