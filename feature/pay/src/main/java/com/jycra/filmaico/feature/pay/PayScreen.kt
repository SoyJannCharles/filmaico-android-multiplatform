package com.jycra.filmaico.feature.pay

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.jycra.filmaico.core.navigation.Platform
import com.jycra.filmaico.core.ui.R
import com.jycra.filmaico.core.ui.component.FilmaicoLogo
import com.jycra.filmaico.core.ui.component.dialog.ConfirmationDialog
import com.jycra.filmaico.core.ui.component.dialog.ErrorDialog
import com.jycra.filmaico.core.ui.component.dialog.ReauthDialog
import com.jycra.filmaico.core.ui.feature.common.InfoGroup
import com.jycra.filmaico.core.ui.feature.common.InfoRow
import com.jycra.filmaico.core.ui.util.extension.asString
import com.jycra.filmaico.domain.user.error.AuthError

@Composable
fun PayScreen(
    uiState: PayUiState,
    platform: Platform,
    onEvent: (PayUiEvent) -> Unit
) {

    if (uiState.showDeleteConfirmation) {
        ConfirmationDialog(
            title = stringResource(R.string.cancel_subscription_dialog_title),
            message = stringResource(R.string.cancel_subscription_dialog_message),
            confirmText = stringResource(R.string.cancel_subscription_dialog_confirm_button),
            isDestructive = true,
            onConfirm = { onEvent(PayUiEvent.OnConfirmDelete) },
            onDismiss = { onEvent(PayUiEvent.OnDismissDelete) }
        )
    }

    if (uiState.showReauthDialog) {
        ReauthDialog(
            isLoading = uiState.isDeleting,
            error = uiState.error?.asString(),
            onConfirm = { password -> onEvent(PayUiEvent.OnReauthConfirm(password)) },
            onDismiss = { onEvent(PayUiEvent.OnReauthDismiss) }
        )
    } else if (uiState.error != null) {
        ErrorDialog(
            message = uiState.error.asString(),
            onDismiss = { onEvent(PayUiEvent.OnErrorDismiss) }
        )
    }

    when (platform) {
        Platform.MOBILE -> PayScreenMobile(
            isDeleting = uiState.isDeleting,
            onCancelClick = {
                onEvent(PayUiEvent.OnCancelClick)
            }
        )
        Platform.TV -> PayScreenTv(
            isDeleting = uiState.isDeleting,
            onCancelClick = {
                onEvent(PayUiEvent.OnCancelClick)
            }
        )
    }

}

@Composable
private fun PayScreenMobile(
    isDeleting: Boolean,
    onCancelClick: () -> Unit
) {

    Scaffold { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colorScheme.surfaceContainerLowest)
                .padding(paddingValues)
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            FilmaicoLogo()

            Column(
                modifier = Modifier
                    .weight(1f),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(
                    style = MaterialTheme.typography.titleLarge,
                    textAlign = TextAlign.Center,
                    text = stringResource(R.string.pay_title)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center,
                    text = stringResource(R.string.pay_instructions)
                )

                Spacer(modifier = Modifier.height(48.dp))

                InfoGroup {
                    InfoRow(label = "CBU", value = "0000168300000019511096", copyable = true)
                    HorizontalDivider(color = MaterialTheme.colorScheme.surfaceBright)
                    InfoRow(label = "Alias", value = "filmaico.app", copyable = true)
                }

                Spacer(modifier = Modifier.height(48.dp))

                if (isDeleting) {
                    CircularProgressIndicator()
                } else {
                    CircularProgressIndicator(modifier = Modifier.size(32.dp))
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        style = MaterialTheme.typography.bodySmall,
                        text = stringResource(R.string.pay_text_waiting_for_confirmation)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    style = MaterialTheme.typography.bodySmall,
                    text = stringResource(R.string.pay_text_waiting_for_confirmation)
                )

                Spacer(modifier = Modifier.height(48.dp))

                OutlinedButton(
                    onClick = onCancelClick,
                    enabled = !isDeleting
                ) {
                    Text(
                        text = stringResource(R.string.pay_button_cancel)
                    )
                }

            }


        }

    }

}

@Composable
private fun PayScreenTv(
    isDeleting: Boolean,
    onCancelClick: () -> Unit
) {

    PayScreenMobile(
        isDeleting = isDeleting,
        onCancelClick = onCancelClick
    )

}