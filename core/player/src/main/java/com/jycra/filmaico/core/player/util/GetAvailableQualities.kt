package com.jycra.filmaico.core.player.util

import androidx.annotation.OptIn
import androidx.media3.common.C
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import com.jycra.filmaico.core.player.model.Quality


@OptIn(UnstableApi::class)
fun Player.getAvailableQualities(): List<Quality> {

    val videoGroups = currentTracks.groups.filter { it.type == C.TRACK_TYPE_VIDEO }
    val numericQualities = mutableListOf<Quality>()

    videoGroups.forEach { group ->
        for (i in 0 until group.length) {
            if (group.isTrackSupported(i)) {
                val format = group.getTrackFormat(i)
                if (format.height > 0) {
                    numericQualities.add(
                        Quality(
                            height = format.height,
                            bitrate = format.bitrate,
                            label = "${format.height}p",
                            isAuto = false
                        )
                    )
                }
            }
        }
    }

    val sortedResolutions = numericQualities
        .distinctBy { it.height }
        .sortedByDescending { it.height }

    val autoOption = Quality(
        height = null,
        bitrate = 0,
        label = "Automático",
        isAuto = true
    )

    return listOf(autoOption) + sortedResolutions

}