package com.jycra.filmaico.feature.signin.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.jycra.filmaico.core.ui.R
import com.jycra.filmaico.core.ui.component.field.FilmaicoTextField
import com.jycra.filmaico.domain.user.error.AuthError
import com.jycra.filmaico.feature.signin.SignInUiEvent
import com.jycra.filmaico.feature.signin.SignInUiState

@Composable
fun SignInForm(
    modifier: Modifier = Modifier,
    uiState: SignInUiState,
    onEvent: (SignInUiEvent) -> Unit
) {

    val focusManager = LocalFocusManager.current

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            style = MaterialTheme.typography.headlineSmall,
            text = stringResource(R.string.signin_title)
        )

        Spacer(modifier = Modifier.height(48.dp))

        FilmaicoTextField(
            value = uiState.email,
            onValueChange = { onEvent(SignInUiEvent.EmailChanged(it)) },
            label = stringResource(R.string.signin_label_email),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            ),
            isError = uiState.error != null
        )

        Spacer(modifier = Modifier.height(8.dp))

        FilmaicoTextField(
            value = uiState.password,
            onValueChange = { onEvent(SignInUiEvent.PasswordChanged(it)) },
            label = stringResource(R.string.signin_label_password),
            isPasswordField = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    focusManager.clearFocus()
                    onEvent(SignInUiEvent.SignInTriggered)
                }
            ),
            isError = uiState.error != null,
            errorMessage = uiState.error?.let { mapAuthErrorToString(it) }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            modifier = Modifier
                .fillMaxWidth(),
            enabled = !uiState.isLoading,
            shape = RoundedCornerShape(8.dp),
            onClick = { onEvent(SignInUiEvent.SignInTriggered) },
        ) {

            if (!uiState.isLoading)
                Text(
                    style = MaterialTheme.typography.titleSmall,
                    text = stringResource(R.string.signin_button),
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

@Composable
fun mapAuthErrorToString(error: AuthError): String {
    return stringResource(
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
}