package com.jycra.filmaico.domain.serie.model

import java.util.Locale

val Serie.localizedName: String
    get() {
        val currentLanguage = Locale.getDefault().language
        return name[currentLanguage]
            ?: name["es"]
            ?: name["en"]
            ?: "Sin Título"
    }

val Serie.localizedSynopsis: String
    get() {
        val currentLanguage = Locale.getDefault().language
        return synopsis[currentLanguage]
            ?: synopsis["es"]
            ?: synopsis["en"]
            ?: "Sin Sinopsis"
    }

val Serie.episodeCount: Int
    get() {
        return seasons.sumOf { season ->
            season.content.size
        }
    }