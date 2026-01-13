package com.jycra.filmaico.domain.movie.model

import java.util.Locale

val MovieCarousel.localizedTitle: String
    get() {
        val currentLanguage = Locale.getDefault().language
        return title[currentLanguage]
            ?: title["es"]
            ?: title["en"]
            ?: "Sin Título"
    }