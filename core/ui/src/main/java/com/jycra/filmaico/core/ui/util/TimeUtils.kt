package com.jycra.filmaico.core.ui.util

import com.jycra.filmaico.core.ui.R
import java.util.concurrent.TimeUnit

fun formatTimeRemaining(diffMillis: Long): UiText {

    val diff = diffMillis.coerceAtLeast(0)
    val days = TimeUnit.MILLISECONDS.toDays(diff)

    return when {

        diff <= 0 -> UiText.StringResource(R.string.subscription_expired)

        days < 1 -> {
            val hours = TimeUnit.MILLISECONDS.toHours(diff)
            if (hours < 1) {
                val mins = TimeUnit.MILLISECONDS.toMinutes(diff)
                UiText.StringResource(R.string.subscription_mins, mins)
            } else if(hours == 1L) {
                UiText.StringResource(R.string.subscription_hour, hours)
            } else {
                UiText.StringResource(R.string.subscription_hours, hours)
            }
        }

        days < 30 -> {
            if (days == 1L)
                UiText.StringResource(R.string.subscription_day, days)
            else
                UiText.StringResource(R.string.subscription_days, days)
        }

        days < 365 -> {
            val months = days / 30
            if (months == 1L)
                UiText.StringResource(R.string.subscription_month, months)
            else
                UiText.StringResource(R.string.subscription_months, months)
        }

        else -> {
            val years = days / 365
            if (years == 1L)
                UiText.StringResource(R.string.subscription_year, years)
            else
                UiText.StringResource(R.string.subscription_years, years)
        }

    }

}

fun formatDurationLabels(minutes: Long): String {

    if (minutes <= 0) return "0 min"

    val hours = minutes / 60
    val minutes = minutes % 60

    return when {
        hours > 0 -> {
            if (minutes > 0) "${hours}h ${minutes}m" else "${hours}h"
        }
        else -> {
            "${minutes.coerceAtLeast(1)} min"
        }
    }

}

fun formatPlaybackTime(millis: Long): String {

    val totalSeconds = millis / 1000

    val hours = totalSeconds / 3600
    val minutes = (totalSeconds % 3600) / 60
    val seconds = totalSeconds % 60

    return if (hours > 0) {
        String.format("%d:%02d:%02d", hours, minutes, seconds)
    } else {
        String.format("%02d:%02d", minutes, seconds)
    }

}