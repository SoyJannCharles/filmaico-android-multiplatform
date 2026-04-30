package com.jycra.filmaico.core.ui.feature.media.util.mapper

import com.jycra.filmaico.core.ui.feature.media.model.UiMediaSeason
import com.jycra.filmaico.core.ui.feature.media.model.UiMedia
import com.jycra.filmaico.core.ui.feature.media.model.UiMediaDetail
import com.jycra.filmaico.core.ui.feature.media.util.variant.MediaCardVariant
import com.jycra.filmaico.core.ui.util.formatDurationLabels
import com.jycra.filmaico.domain.media.model.ContentStatus
import com.jycra.filmaico.domain.media.model.Media
import com.jycra.filmaico.domain.media.model.MediaType
import com.jycra.filmaico.domain.media.util.extesion.localizedImageUrl
import com.jycra.filmaico.domain.media.util.extesion.localizedName
import com.jycra.filmaico.domain.media.util.extesion.localizedSynopsis
import java.util.Calendar

fun Media.toUiMedia(forcedVariant: MediaCardVariant? = null): UiMedia {

    val defaultVariant = when (this.ownerMediaType) {
        MediaType.CHANNEL -> MediaCardVariant.BACKDROP_HORIZONTAL
        else -> MediaCardVariant.POSTER_VERTICAL
    }

    val name = when (this) {
        is Media.Asset -> {
            this.localizedName.ifEmpty { "Episodio ${this.number}" }
        }
        is Media.Container -> {
            this.localizedName
        }
    }

    val label = when (this) {
        is Media.Asset -> {
            when {

                this.mediaType == MediaType.CHANNEL -> null

                ownerMediaType == MediaType.MOVIE -> {
                    formatDurationLabels(this.duration ?: 0)
                }

                this.mediaType == MediaType.EPISODE -> {
                    "Episodio ${this.number} | ${formatDurationLabels(this.duration ?: 0)}"
                }

                this.mediaType == MediaType.OVA -> {
                    "Ova | ${formatDurationLabels(this.duration ?: 0)}"
                }

                this.mediaType == MediaType.MOVIE -> {
                    "Película | ${formatDurationLabels(this.duration ?: 0)}"
                }

                else -> null
            }

        }

        is Media.Container -> {

            val statusLabel = when (this.status) {
                ContentStatus.AIRING -> "En Emisión"
                ContentStatus.FINISHED -> "Finalizado"
                ContentStatus.ON_HIATUS -> "Pausado"
                else -> "Desconocido"
            }

            statusLabel

        }

    }

    return UiMedia(
        id = this.id,
        mediaType = this.mediaType,
        variant = forcedVariant ?: defaultVariant,
        name = name,
        label = label,
        imageUrl = this.localizedImageUrl,
        epgId = (this as? Media.Asset)?.epgId,
        order = (this as? Media.Asset)?.number ?: 0,
        duration = (this as? Media.Asset)?.duration ?: 0,
        lastPosition = (this as? Media.Asset)?.lastPosition ?: 0,
        isFinished = (this as? Media.Asset)?.isFinished ?: false
    )

}

fun Media.Container.toUiDetail(selectedSeasonId: String?): UiMediaDetail {

    val activeSeason = this.seasons.find { it.id == selectedSeasonId }
        ?: this.seasons.firstOrNull()

    val releaseYear = firstAirDate?.let { millis ->
        val calendar = Calendar.getInstance().apply { timeInMillis = millis }
        calendar.get(Calendar.YEAR).toString()
    } ?: airDate?.let { millis ->
        val calendar = Calendar.getInstance().apply { timeInMillis = millis }
        calendar.get(Calendar.YEAR).toString()
    } ?: ""

    return UiMediaDetail(
        id = this.id,
        name = this.localizedName,
        synopsis = this.localizedSynopsis,
        imageUrl = this.localizedImageUrl,
        releaseYear = releaseYear,
        mediaType = this.mediaType,
        status = this.status,
        seasonCount = this.seasons.size,
        episodeCount = this.seasons.sumOf { it.episodes.size },
        seasons = this.seasons.map { season ->
            UiMediaSeason(
                id = season.id,
                title = season.name.getValue("es"),
                isSelected = season.id == activeSeason?.id
            )
        },
        selectedSeasonContents = activeSeason?.episodes?.map {
            it.toUiMedia(forcedVariant = MediaCardVariant.THUMBNAIL_STANDARD)
        } ?: emptyList()
    )

}