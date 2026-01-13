package com.jycra.filmaico.core.reporter

interface ErrorReporter {

    fun recordException(throwable: Throwable, context: Map<String, Any> = emptyMap())

}