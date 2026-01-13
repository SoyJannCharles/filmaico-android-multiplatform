package com.jycra.filmaico.domain.serie.model

import com.jycra.filmaico.domain.stream.model.Stream

sealed interface SerieContent {

    val id: String
    val type: String
    val order: Int
    val name: Map<String, String>
    val duration: Long
    val thumbnailUrl: String
    val sources: List<Stream>

    data class Episode(
        override val id: String,
        override val order: Int,
        override val name: Map<String, String>,
        override val duration: Long,
        override val thumbnailUrl: String,
        override val sources: List<Stream>,
        val episodeNumber: Int
    ) : SerieContent {
        override val type: String = "episode"
    }

}