package com.jycra.filmaico.data.stream.util

import androidx.core.net.toUri

fun String.toStableEdgeKey(): String {
    return try {
        val uri = this.toUri()
        "${uri.scheme}://${uri.host}${uri.path}"
    } catch (e: Exception) {
        this
    }
}