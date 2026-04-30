package com.jycra.filmaico.core.ui.feature.media.model

import com.jycra.filmaico.core.ui.feature.media.util.variant.MediaCardVariant
import com.jycra.filmaico.domain.media.model.MediaType
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class UiMedia(
    val id: String,
    val mediaType: MediaType,
    val variant: MediaCardVariant,
    val name: String,
    val label: String? = null,
    val imageUrl: String,
    val epgId: String? = null,
    val epgTitle: String? = null,
    val epgDescription: String? = null,
    val epgStartTime: Long? = null,
    val epgEndTime: Long? = null,
    val order: Int = 0,
    val duration: Long = 0,
    val lastPosition: Long = 0,
    val isFinished: Boolean = false
) {

    val epgTimeLabel: String
        get() = if (epgStartTime != null && epgEndTime != null) {
            "${formatTime(epgStartTime)} - ${formatTime(epgEndTime)}"
        } else {
            ""
        }

    val durationInMs: Long
        get() = duration * 60L * 1000L

    private fun formatTime(timestamp: Long): String {
        val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
        return sdf.format(Date(timestamp))
    }

}