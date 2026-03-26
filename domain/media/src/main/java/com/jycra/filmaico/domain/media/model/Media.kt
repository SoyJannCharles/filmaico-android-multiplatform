package com.jycra.filmaico.domain.media.model

import com.jycra.filmaico.domain.common.content.model.ContentStatus
import com.jycra.filmaico.domain.media.model.stream.Stream

sealed interface Media {

    val id: String
    val name: Map<String, String>
    val synopsis: Map<String, String>?
    val imageUrl: Map<String, String>
    val tags: List<String>
    val mediaType: MediaType
    val ownerMediaType: MediaType
    val isSaved: Boolean

    data class Container(
        override val id: String,
        override val name: Map<String, String>,
        override val synopsis: Map<String, String>,
        override val imageUrl: Map<String, String>,
        override val tags: List<String>,
        override val mediaType: MediaType,
        override val ownerMediaType: MediaType,
        override val isSaved: Boolean = false,
        val firstAirDate: Long?,
        val lastAirDate: Long?,
        val airDate: Long?,
        val status: ContentStatus = ContentStatus.UNKNOWN,
        val seasons: List<MediaSeason> = emptyList()
    ) : Media

    data class Asset(
        override val id: String,
        override val name: Map<String, String>,
        override val synopsis: Map<String, String>? = null,
        override val imageUrl: Map<String, String>,
        override val tags: List<String>,
        override val mediaType: MediaType,
        override val ownerMediaType: MediaType,
        override val isSaved: Boolean = false,
        val ownerId: String?,
        val seasonId: String?,
        val airDate: Long? = null,
        val duration: Long?,
        val number: Int?,
        val sources: List<Stream>,
        val isLive: Boolean = false,
        val lastPosition: Long = 0,
        val isFinished: Boolean = false
    ) : Media

}