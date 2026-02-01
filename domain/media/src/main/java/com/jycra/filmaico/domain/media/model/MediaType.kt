package com.jycra.filmaico.domain.media.model

enum class MediaType(val value: String) {

    CHANNEL("channel"),
    MOVIE("movie"),
    SERIE("serie"),
    ANIME("anime"),

    EPISODE("episode"),
    OVA("ova");

    companion object {

        fun fromString(mediaType: String?): MediaType {
            return entries.find { it.value == mediaType?.lowercase() } ?: EPISODE
        }

    }

}