package com.jycra.filmaico.domain.movie.model

import java.util.Locale

val Movie.localizedName: String
    get() {
        val currentLanguage = Locale.getDefault().language
        return name[currentLanguage]
            ?: name["es"]
            ?: name["en"]
            ?: "Sin Sinopsis"
    }

val Movie.localizedSynopsis: String
    get() {
        val currentLanguage = Locale.getDefault().language
        return synopsis[currentLanguage]
            ?: synopsis["es"]
            ?: synopsis["en"]
            ?: "Sin Sinopsis"
    }