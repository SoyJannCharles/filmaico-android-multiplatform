package com.jycra.filmaico.domain.media.model

data class Epg(
    val epgId: String,
    val title: String,
    val description: String?,
    val startTime: Long,
    val endTime: Long
)