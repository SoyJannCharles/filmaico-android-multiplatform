package com.jycra.filmaico.domain.serie.model

import java.util.Locale

val SerieSeason.localizedName: String
    get() {
        val currentLanguage = Locale.getDefault().language
        return name[currentLanguage]
            ?: name["es"]
            ?: name["en"]
            ?: name.values.firstOrNull()
            ?: "Sin Título"
    }