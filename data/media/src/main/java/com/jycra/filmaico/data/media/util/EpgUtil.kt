package com.jycra.filmaico.data.media.util

import java.text.SimpleDateFormat
import java.util.Locale

fun parseEpgDate(dateString: String): Long {
    return try {
        SimpleDateFormat("yyyyMMddHHmmss Z", Locale.US)
            .parse(dateString)?.time ?: 0L
    } catch (e: Exception) {
        0L
    }
}