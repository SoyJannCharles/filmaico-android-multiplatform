package com.jycra.filmaico.domain.user.util

sealed class AuthResult<out S, out F> {
    data class Success<out S>(val data: S) : AuthResult<S, Nothing>()
    data class Failure<out F>(val failure: F) : AuthResult<Nothing, F>()
}