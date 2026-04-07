package com.jycra.filmaico.core.player.model

data class Quality(
    val height: Int? = null,
    val bitrate: Int,
    val label: String,
    val isAuto: Boolean = false
) {

    val bitrateLabel: String
        get() = if (bitrate > 0) {
            val mbps = bitrate / 1_000_000f
            "%.1f Mbps".format(mbps)
        } else {
            ""
        }

}