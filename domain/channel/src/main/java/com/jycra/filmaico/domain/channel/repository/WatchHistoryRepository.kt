package com.jycra.filmaico.domain.channel.repository

import com.jycra.filmaico.domain.channel.model.Channel
import kotlinx.coroutines.flow.Flow

interface WatchHistoryRepository {

    fun getTopWatchedChannels(): Flow<List<Channel>>

    suspend fun saveWatchTime(channelId: String, watchTimeInSeconds: Long)

}