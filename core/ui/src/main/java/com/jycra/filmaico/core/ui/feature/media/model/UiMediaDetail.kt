package com.jycra.filmaico.core.ui.feature.media.model

import com.jycra.filmaico.core.ui.feature.media.model.UiMediaSeason
import com.jycra.filmaico.domain.common.content.model.ContentStatus
import com.jycra.filmaico.domain.media.model.MediaType

data class UiMediaDetail(
    val id: String,
    val mediaType: MediaType,
    val name: String,
    val synopsis: String,
    val imageUrl: String,
    val releaseYear: String,
    val status: ContentStatus = ContentStatus.UNKNOWN,
    val seasonCount: Int = 0,
    val episodeCount: Int = 0,
    val seasons: List<UiMediaSeason> = emptyList(),
    val selectedSeasonContents: List<UiMedia> = emptyList()
)