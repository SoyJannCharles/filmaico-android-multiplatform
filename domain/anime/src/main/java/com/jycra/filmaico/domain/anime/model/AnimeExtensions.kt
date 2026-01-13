package com.jycra.filmaico.domain.anime.model

import java.util.Locale

val Anime.localizedName: String
    get() {
        val currentLanguage = Locale.getDefault().language
        return name[currentLanguage]
            ?: name["es"]
            ?: name["en"]
            ?: "Sin Nombre"
    }

val Anime.localizedSynopsis: String
    get() {
        val currentLanguage = Locale.getDefault().language
        return synopsis[currentLanguage]
            ?: synopsis["es"]
            ?: synopsis["en"]
            ?: "Sin Sinopsis"
    }

val Anime.episodeCount: Int
    get() {
        return seasons.sumOf { season ->
            season.content.size
        }
    }