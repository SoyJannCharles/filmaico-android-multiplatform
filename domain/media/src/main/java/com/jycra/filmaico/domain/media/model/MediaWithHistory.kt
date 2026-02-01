package com.jycra.filmaico.domain.media.model

import com.jycra.filmaico.domain.history.model.MediaProgress

data class MediaWithHistory(
    val media: Media,
    val history: MediaProgress? = null
)