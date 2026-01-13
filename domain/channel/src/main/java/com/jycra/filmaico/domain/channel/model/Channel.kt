package com.jycra.filmaico.domain.channel.model

import com.jycra.filmaico.domain.stream.model.Stream

data class Channel(
    val id: String = "",
    val name: Map<String, String> = emptyMap(),
    val iconUrl: String = "",
    val tags: List<String> = emptyList(),
    val sources: List<Stream> = emptyList()
)