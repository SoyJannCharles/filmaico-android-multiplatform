package com.jycra.filmaico.domain.channel.error

class DecryptionException(
    message: String,
    cause: Throwable? = null
) : Exception(message, cause)