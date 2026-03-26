package com.jycra.filmaico.domain.media.util.extesion

import com.jycra.filmaico.domain.media.model.Media
import com.jycra.filmaico.domain.media.model.MediaCarousel
import com.jycra.filmaico.domain.media.model.metadata.VideoMetadata
import java.util.Locale

val Media.localizedName: String
    get() {
        val currentLanguage = Locale.getDefault().language
        return name[currentLanguage]
            ?: ""
    }

val Media.localizedSynopsis: String
    get() {
        val currentLanguage = Locale.getDefault().language
        return synopsis?.get(currentLanguage)
            ?: ""
    }

val Media.localizedImageUrl: String
    get() {
        val currentLanguage = Locale.getDefault().language
        return imageUrl[currentLanguage]
            ?: imageUrl["default"]
            ?: ""
    }

val VideoMetadata.localizedName: String
    get() {
        val currentLanguage = Locale.getDefault().language
        return name[currentLanguage]
            ?: ""
    }

val MediaCarousel.localizedTitle: String
    get() {
        val currentLanguage = Locale.getDefault().language
        return title[currentLanguage]
            ?: ""
    }