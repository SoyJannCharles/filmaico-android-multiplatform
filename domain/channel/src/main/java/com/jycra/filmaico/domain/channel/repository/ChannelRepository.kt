package com.jycra.filmaico.domain.channel.repository

import com.jycra.filmaico.domain.channel.model.Channel
import com.jycra.filmaico.domain.channel.model.ChannelCarousel
import kotlinx.coroutines.flow.Flow

interface ChannelRepository {

    fun getChannelCarousels(): Flow<List<ChannelCarousel>>
    suspend fun getChannelById(id: String): Channel?

}