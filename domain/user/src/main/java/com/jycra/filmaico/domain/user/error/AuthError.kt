package com.jycra.filmaico.domain.user.error

sealed class AuthError {

    data object EmailAlreadyInUse : AuthError()
    data object WeakPassword : AuthError()

    data object InvalidCredentials : AuthError()
    data object UserNotFound : AuthError()
    data object AccountDisabled : AuthError()

    data object RequiresRecentLogin : AuthError()
    data object TooManyDevices : AuthError()

    data object PermissionDenied : AuthError()
    data object TooManyRequests : AuthError()
    data object NetworkError : AuthError()
    data object ServerError : AuthError()

    data object NullUserAfterAuthSuccess : AuthError()

    data object Unknown : AuthError()

}