package com.jycra.filmaico.core.ui.feature.media.util.variant

import com.jycra.filmaico.core.device.Platform
import com.jycra.filmaico.core.ui.feature.media.util.variant.SpecialCarouselType

enum class MediaCardVariant {
    POSTER_VERTICAL,
    BACKDROP_HORIZONTAL,
    THUMBNAIL_STANDARD,
    THUMBNAIL_COMPACT_ROW;

    companion object {

        fun getVariantByCarouselId(
            carouselId: String,
            platform: Platform? = null,
            fallback: MediaCardVariant
        ): MediaCardVariant {
            return when (carouselId) {
                SpecialCarouselType.COLLECTION.value ->
                    SpecialCarouselType.COLLECTION.getPreferredVariant(platform)
                SpecialCarouselType.HISTORY.value ->
                    SpecialCarouselType.HISTORY.getPreferredVariant()
                else -> fallback
            }
        }

    }

}