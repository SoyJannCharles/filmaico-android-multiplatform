package com.jycra.filmaico.core.ui.feature.search

import com.jycra.filmaico.core.ui.feature.browse.model.UiBrowseCarouselItem
import com.jycra.filmaico.core.ui.feature.browse.util.CardOrientation

data class UiSearchCarousel(
    val title: String,
    val items: List<UiBrowseCarouselItem>,
    val orientation: CardOrientation,
    val carouselIndex: Int,
    val contentType: String
)