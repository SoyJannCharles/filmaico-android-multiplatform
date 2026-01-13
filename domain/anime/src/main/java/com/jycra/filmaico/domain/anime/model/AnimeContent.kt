package com.jycra.filmaico.domain.anime.model

import com.jycra.filmaico.domain.stream.model.Stream

sealed interface AnimeContent {

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
    ) : AnimeContent {
        override val type: String = "episode"
    }

    data class Movie(
        override val id: String,
        override val order: Int,
        override val name: Map<String, String>,
        override val duration: Long,
        override val thumbnailUrl: String,
        override val sources: List<Stream>
    ) : AnimeContent {
        override val type: String = "movie"
    }

    data class Ova(
        override val id: String,
        override val order: Int,
        override val name: Map<String, String>,
        override val duration: Long,
        override val thumbnailUrl: String,
        override val sources: List<Stream>
    ) : AnimeContent {
        override val type: String = "ova"
    }

}