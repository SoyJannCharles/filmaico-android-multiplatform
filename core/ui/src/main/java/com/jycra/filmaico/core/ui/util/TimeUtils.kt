package com.jycra.filmaico.core.ui.util

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