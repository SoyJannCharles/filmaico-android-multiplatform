package com.jycra.filmaico.domain.channel.model

import java.util.Locale

val ChannelCarousel.localizedTitle: String
    get() {
        val currentLanguage = Locale.getDefault().language
        return title[currentLanguage]
            ?: title["es"]
            ?: title["en"]
            ?: "Sin Título"
    }