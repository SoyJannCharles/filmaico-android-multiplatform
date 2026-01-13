package com.jycra.filmaico.core.ui.feature.detail.content

val UiContent.formattedDuration: String
    get() {

        val hours = duration / 3600
        val minutes = duration % 3600 / 60
        val seconds = duration % 60

        return if (hours > 0)
            String.format("%d:%02d:%02d", hours, minutes, seconds)
        else
            String.format("%02d:%02d", minutes, seconds)

    }