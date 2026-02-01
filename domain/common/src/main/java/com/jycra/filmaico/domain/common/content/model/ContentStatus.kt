package com.jycra.filmaico.domain.common.content.model

enum class ContentStatus(val value: String) {

    AIRING("airing"),
    FINISHED("finished"),
    ON_HIATUS("on_hiatus"),
    UNKNOWN("unknown");

    companion object {

        fun fromValue(value: String?): ContentStatus {
            return entries.find { it.value == value } ?: UNKNOWN
        }

    }

}