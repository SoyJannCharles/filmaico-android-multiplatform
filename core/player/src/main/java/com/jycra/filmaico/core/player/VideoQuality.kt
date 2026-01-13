package com.jycra.filmaico.core.player

data class VideoQuality(
    val height: Int,
    val bitrate: Int,
    val label: String, // Ej: "1080p", "Auto"
    val isAuto: Boolean = false
) {

    val bitrateLabel: String
        get() = if (bitrate > 0) {
            val mbps = bitrate / 1_000_000f // Dividimos por 1 millón
            "%.1f Mbps".format(mbps) // Ej: "5.4 Mbps"
        } else {
            ""
        }

}