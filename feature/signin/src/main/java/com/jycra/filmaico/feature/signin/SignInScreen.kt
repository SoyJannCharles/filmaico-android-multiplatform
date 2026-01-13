package com.jycra.filmaico.feature.signin

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
import com.jycra.filmaico.feature.signin.component.SignInForm
import com.jycra.filmaico.feature.signin.component.SignUpActionLink

@Composable
fun SignInScreen(
    uiState: SignInUiState,
    platform: Platform,
    onEvent: (SignInUiEvent) -> Unit
) {

    when (platform) {
        Platform.MOBILE -> SignInScreenMobile(
            uiState = uiState,
            onEvent = onEvent
        )
        Platform.TV -> SignInScreenTv(
            uiState = uiState,
            onEvent = onEvent
        )
    }

}

@Composable
private fun SignInScreenMobile(
    uiState: SignInUiState,
    onEvent: (SignInUiEvent) -> Unit
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

                SignInForm(
                    modifier = Modifier
                        .weight(1f),
                    uiState = uiState,
                    onEvent = onEvent
                )

                SignUpActionLink(
                    onEvent = onEvent
                )

            }

        }

    }

}

@Composable
private fun SignInScreenTv(
    uiState: SignInUiState,
    onEvent: (SignInUiEvent) -> Unit
) {

    SignInScreenMobile(
        uiState = uiState,
        onEvent = onEvent
    )

}