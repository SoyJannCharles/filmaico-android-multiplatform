package com.jycra.filmaico.domain.anime.model

import java.util.Locale

val AnimeCarousel.localizedTitle: String
    get() {
        val currentLanguage = Locale.getDefault().language
        return title[currentLanguage]
            ?: title["es"]
            ?: title["en"]
            ?: "Sin Título"
    }