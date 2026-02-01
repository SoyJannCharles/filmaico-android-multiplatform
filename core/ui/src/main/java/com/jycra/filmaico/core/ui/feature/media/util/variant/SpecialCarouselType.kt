package com.jycra.filmaico.core.ui.feature.media.util.variant

import com.jycra.filmaico.core.device.Platform

enum class SpecialCarouselType(val value: String) {

    COLLECTION("collection"),
    HISTORY("continue_watching");

    fun getPreferredVariant(platform: Platform? = null): MediaCardVariant {
        return when (this) {
            COLLECTION -> {
                when (platform) {
                    Platform.MOBILE -> MediaCardVariant.THUMBNAIL_COMPACT_ROW
                    Platform.TV -> MediaCardVariant.THUMBNAIL_STANDARD
                    else -> MediaCardVariant.THUMBNAIL_STANDARD
                }
            }
            HISTORY -> MediaCardVariant.THUMBNAIL_STANDARD
        }
    }

}