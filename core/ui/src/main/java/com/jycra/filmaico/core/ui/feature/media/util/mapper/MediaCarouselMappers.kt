package com.jycra.filmaico.core.ui.feature.media.util.mapper

import com.jycra.filmaico.core.ui.feature.media.model.UiMediaCarousel
import com.jycra.filmaico.domain.media.model.Media
import com.jycra.filmaico.domain.media.model.MediaCarousel
import com.jycra.filmaico.domain.media.model.MediaType
import com.jycra.filmaico.domain.media.util.extesion.localizedTitle

fun MediaCarousel.toUiCarousels() = UiMediaCarousel(
    id = this.id,
    title = this.localizedTitle,
    items = this.items.map { it.toUiMedia() }
)

fun Map<MediaType, List<Media>>.toUiCarousels(): List<UiMediaCarousel> {
    return this.map { (type, items) ->
        UiMediaCarousel(
            id = "search_carousel_${type.value}",
            title = when (type) {
                MediaType.CHANNEL -> "Canales de TV"
                MediaType.MOVIE -> "Películas"
                MediaType.SERIE -> "Series"
                MediaType.ANIME -> "Anime"
                else -> ""
            },
            items = items.map { it.toUiMedia() }
        )
    }
}