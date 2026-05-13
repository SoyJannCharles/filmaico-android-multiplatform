package com.jycra.filmaico.data.stream.util.extension

import androidx.core.net.toUri

fun String.toHostOnly(): String? = try {
    this.toUri().host?.lowercase()
} catch (e: Exception) { null }