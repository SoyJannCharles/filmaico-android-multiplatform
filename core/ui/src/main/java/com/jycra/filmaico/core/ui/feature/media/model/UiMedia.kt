package com.jycra.filmaico.core.ui.feature.media.model

import com.jycra.filmaico.core.ui.feature.media.util.variant.MediaCardVariant
import com.jycra.filmaico.domain.media.model.MediaType

data class UiMedia(
    val id: String,
    val mediaType: MediaType,
    val variant: MediaCardVariant,
    val name: String,
    val label: String? = null,
    val imageUrl: String,
    val order: Int = 0,
    val duration: Long = 0,
    val lastPosition: Long = 0,
    val isFinished: Boolean = false
) {

    val durationInMs: Long
        get() = duration * 60L * 1000L

}