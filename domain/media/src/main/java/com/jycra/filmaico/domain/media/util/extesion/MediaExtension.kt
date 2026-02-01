package com.jycra.filmaico.domain.media.util.extesion

import com.jycra.filmaico.domain.media.model.Media
import com.jycra.filmaico.domain.media.model.MediaCarousel
import java.util.Locale

val Media.localizedName: String
    get() {
        val currentLanguage = Locale.getDefault().language
        return name[currentLanguage]
            ?: name["es"]
            ?: name["en"]
            ?: "Sin Nombre"
    }

val Media.Container.localizedSynopsis: String
    get() {
        val currentLanguage = Locale.getDefault().language
        return synopsis[currentLanguage]
            ?: synopsis["es"]
            ?: synopsis["en"]
            ?: "Sin descripción disponible."
    }

val MediaCarousel.localizedTitle: String
    get() {
        val currentLanguage = Locale.getDefault().language
        return title[currentLanguage]
            ?: title["es"]
            ?: title["en"]
            ?: "Sin Título"
    }