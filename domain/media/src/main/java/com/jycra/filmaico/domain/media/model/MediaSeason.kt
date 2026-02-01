package com.jycra.filmaico.domain.media.model

data class MediaSeason(
    val id: String,
    val ownerId: String,
    val number: Int,
    val name: Map<String, String>,
    val episodes: List<Media.Asset> = emptyList()
)