package com.jycra.filmaico.core.player.util

import androidx.media3.common.Player
import com.jycra.filmaico.core.player.model.Quality

fun Player.setVideoQuality(quality: Quality) {

    val parameters = trackSelectionParameters.buildUpon()

    if (quality.isAuto) {
        parameters
            .clearVideoSizeConstraints()
            .setMaxVideoBitrate(Int.MAX_VALUE)
            .clearOverrides()
    } else {
        quality.height?.let { height ->
            parameters
                .setMaxVideoSize(height * 16 / 9, height)
                .setMinVideoSize(0, height)
                .setMaxVideoBitrate(Int.MAX_VALUE)
        }
    }

    trackSelectionParameters = parameters.build()

}