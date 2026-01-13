package com.jycra.filmaico.feature.search.util

import com.jycra.filmaico.core.navigation.ContentType
import com.jycra.filmaico.core.ui.feature.browse.model.UiBrowseCarouselItem
import com.jycra.filmaico.core.ui.feature.browse.util.CardOrientation
import com.jycra.filmaico.core.ui.feature.search.UiSearchCarousel
import com.jycra.filmaico.domain.anime.model.localizedName
import com.jycra.filmaico.domain.channel.model.localizedName
import com.jycra.filmaico.domain.movie.model.localizedName
import com.jycra.filmaico.domain.search.model.SearchResults
import com.jycra.filmaico.domain.serie.model.localizedName

fun SearchResults.toUiCarousels(): List<UiSearchCarousel> {
    val carousels = mutableListOf<UiSearchCarousel>()
    var carouselIndex = 0

    // Canales
    if (channels.isNotEmpty()) {
        carousels.add(
            UiSearchCarousel(
                title = "Canales",
                items = channels.map { channel ->
                    UiBrowseCarouselItem(
                        id = channel.id,
                        name = channel.localizedName,
                        imageUrl = channel.iconUrl
                    )
                },
                orientation = CardOrientation.HORIZONTAL,
                carouselIndex = carouselIndex++,
                contentType = ContentType.CHANNEL
            )
        )
    }

    // Películas
    if (movies.isNotEmpty()) {
        carousels.add(
            UiSearchCarousel(
                title = "Películas",
                items = movies.map { movie ->
                    UiBrowseCarouselItem(
                        id = movie.id,
                        name = movie.localizedName,
                        imageUrl = movie.coverUrl
                    )
                },
                orientation = CardOrientation.VERTICAL,
                carouselIndex = carouselIndex++,
                contentType = ContentType.MOVIE
            )
        )
    }

    // Series
    if (series.isNotEmpty()) {
        carousels.add(
            UiSearchCarousel(
                title = "Series",
                items = series.map { serie ->
                    UiBrowseCarouselItem(
                        id = serie.id,
                        name = serie.localizedName,
                        imageUrl = serie.coverUrl
                    )
                },
                orientation = CardOrientation.VERTICAL,
                carouselIndex = carouselIndex++,
                contentType = ContentType.SERIE
            )
        )
    }

    // Animes
    if (animes.isNotEmpty()) {
        carousels.add(
            UiSearchCarousel(
                title = "Animes",
                items = animes.map { anime ->
                    UiBrowseCarouselItem(
                        id = anime.id,
                        name = anime.localizedName,
                        imageUrl = anime.coverUrl
                    )
                },
                orientation = CardOrientation.VERTICAL,
                carouselIndex = carouselIndex++,
                contentType = ContentType.ANIME
            )
        )
    }

    return carousels
}