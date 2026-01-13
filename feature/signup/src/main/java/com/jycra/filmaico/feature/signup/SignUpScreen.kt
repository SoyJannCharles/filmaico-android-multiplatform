package com.jycra.filmaico.feature.signup

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jycra.filmaico.core.navigation.Platform
import com.jycra.filmaico.core.ui.component.FilmaicoLogo
import com.jycra.filmaico.feature.signup.component.SignInActionLink
import com.jycra.filmaico.feature.signup.component.SignUpForm

@Composable
fun SignUpScreen(
    uiState: SignUpUiState,
    platform: Platform,
    onEvent: (SignUpUiEvent) -> Unit
) {

    when (platform) {
        Platform.MOBILE -> SignUpScreenMobile(
            uiState = uiState,
            onEvent = onEvent
        )
        Platform.TV -> SignUpScreenTv(
            uiState = uiState,
            onEvent = onEvent
        )
    }

}

@Composable
private fun SignUpScreenMobile(
    uiState: SignUpUiState,
    onEvent: (SignUpUiEvent) -> Unit
) {

    Scaffold(
        containerColor = MaterialTheme.colorScheme.surfaceContainerLowest
    ) { paddingValues ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .imePadding(),
            contentAlignment = Alignment.Center
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(all = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {

                FilmaicoLogo()

                SignUpForm(
                    modifier = Modifier
                        .weight(1f),
                    uiState = uiState,
                    onEvent = onEvent
                )

                SignInActionLink(
                    onEvent = onEvent
                )

            }

        }

    }

}

@Composable
private fun SignUpScreenTv(
    uiState: SignUpUiState,
    onEvent: (SignUpUiEvent) -> Unit
) {

    SignUpScreenMobile(uiState, onEvent)

}