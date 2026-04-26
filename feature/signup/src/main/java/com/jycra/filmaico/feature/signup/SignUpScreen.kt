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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.jycra.filmaico.core.ui.R
import com.jycra.filmaico.core.ui.component.FilmaicoLogo
import com.jycra.filmaico.core.ui.feature.auth.ActionLink
import com.jycra.filmaico.feature.signup.component.SignUpForm

@Composable
fun SignUpScreen(
    uiState: SignUpUiState,
    onEvent: (SignUpUiEvent) -> Unit
) {

    Screen(
        uiState = uiState,
        onEvent = onEvent
    )

}

@Composable
private fun Screen(
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

                ActionLink(
                    questionText = stringResource(R.string.signup_text_i_have_account),
                    actionText = stringResource(R.string.signup_link_go_to_signin),
                    onActionClick = { onEvent(SignUpUiEvent.SignInTriggered) }
                )

            }

        }

    }

}