package com.jycra.filmaico.feature.signup.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.jycra.filmaico.core.ui.R
import com.jycra.filmaico.core.ui.component.field.FormTextField
import com.jycra.filmaico.domain.user.error.AuthError
import com.jycra.filmaico.feature.signup.SignUpUiEvent
import com.jycra.filmaico.feature.signup.SignUpUiState

@Composable
fun SignUpForm(
    modifier: Modifier = Modifier,
    uiState: SignUpUiState,
    onEvent: (SignUpUiEvent) -> Unit

) {

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            style = MaterialTheme.typography.headlineSmall,
            text = stringResource(R.string.signup_title)
        )

        Spacer(modifier = Modifier.height(48.dp))

        FormTextField(
            value = uiState.email,
            onValueChange = { onEvent(SignUpUiEvent.EmailChange(it)) },
            label = stringResource(R.string.signup_label_email),
            isError = uiState.error != null
        )

        Spacer(modifier = Modifier.height(8.dp))

        FormTextField(
            visualTransformation = PasswordVisualTransformation(),
            value = uiState.password,
            onValueChange = { onEvent(SignUpUiEvent.PasswordChange(it)) },
            label = stringResource(R.string.signup_label_password),
            isError = uiState.error != null
        )

        uiState.error?.let { error ->

            val message = stringResource(
                when (error) {
                    AuthError.EmailAlreadyInUse -> R.string.auth_error_email_already_in_use
                    AuthError.WeakPassword -> R.string.auth_error_weak_password
                    AuthError.InvalidCredentials -> R.string.auth_error_invalid_credentials
                    AuthError.UserNotFound -> R.string.auth_error_user_not_found
                    AuthError.AccountDisabled -> R.string.auth_error_account_disabled
                    AuthError.RequiresRecentLogin -> R.string.auth_error_requires_recent_login
                    AuthError.TooManyDevices -> R.string.auth_error_too_many_devices
                    AuthError.PermissionDenied -> R.string.auth_error_permission_denied
                    AuthError.TooManyRequests -> R.string.auth_error_too_many_requests
                    AuthError.NetworkError -> R.string.auth_error_network_error
                    AuthError.ServerError -> R.string.auth_error_server_error
                    AuthError.NullUserAfterAuthSuccess -> R.string.auth_error_null_user
                    AuthError.Unknown -> R.string.auth_error_unknown
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.error,
                text = message
            )

        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            modifier = Modifier
                .fillMaxWidth(),
            enabled = !uiState.isLoading,
            shape = RoundedCornerShape(8.dp),
            onClick = { onEvent(SignUpUiEvent.SignUpTriggered) },
        ) {

            if (!uiState.isLoading)
                Text(
                    style = MaterialTheme.typography.titleSmall,
                    text = stringResource(R.string.signup_button),
                )
            else
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = MaterialTheme.colorScheme.primary,
                    strokeWidth = 2.dp
                )

        }

    }

}