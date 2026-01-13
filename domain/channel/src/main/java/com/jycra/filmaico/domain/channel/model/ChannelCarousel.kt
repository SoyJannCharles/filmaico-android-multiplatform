package com.jycra.filmaico.domain.channel.model

data class ChannelCarousel(
    val id: String,
    val title: Map<String, String>,
    val channels: List<Channel>
)