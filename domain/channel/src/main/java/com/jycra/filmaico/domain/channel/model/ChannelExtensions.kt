package com.jycra.filmaico.domain.channel.model

import java.util.Locale

val Channel.localizedName: String
    get() {
        val currentLanguage = Locale.getDefault().language
        return name[currentLanguage]
            ?: name["es"]
            ?: name["en"]
            ?: "Sin Nombre"
    }