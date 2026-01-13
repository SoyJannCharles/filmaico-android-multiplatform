package com.jycra.filmaico.core.ui.util.extension

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.jycra.filmaico.core.ui.R
import com.jycra.filmaico.domain.user.error.AuthError

@Composable
fun AuthError.asString(): String {
    return stringResource(
        when (this) {
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