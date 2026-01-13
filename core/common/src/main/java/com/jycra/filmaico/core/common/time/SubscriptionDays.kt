package com.jycra.filmaico.core.common.time

fun formatDaysRemaining(days: Int?): String {

    if (days == null) return ""

    return when {
        days < 30 -> "$days días"
        days < 365 -> {
            val months = days / 30
            if (months == 1) "1 mes" else "$months meses"
        }
        else -> {
            val years = days / 365
            if (years == 1) "1 año" else "$years años"
        }
    }

}