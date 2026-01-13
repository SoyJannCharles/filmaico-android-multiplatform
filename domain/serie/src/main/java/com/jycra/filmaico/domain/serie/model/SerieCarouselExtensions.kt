package com.jycra.filmaico.domain.serie.model

import java.util.Locale

val SerieCarousel.localizedTitle: String
    get() {
        val currentLanguage = Locale.getDefault().language
        return title[currentLanguage]
            ?: title["es"]
            ?: title["en"]
            ?: "Sin Título"
    }