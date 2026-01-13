package com.jycra.filmaico.domain.anime.model

import java.util.Locale

val AnimeSeason.localizedName: String
    get() {
        val currentLanguage = Locale.getDefault().language
        return name[currentLanguage]
            ?: name["es"]
            ?: name["en"]
            ?: "Sin Sinopsis"
    }