package com.jycra.filmaico.data.channel.repository

import com.google.gson.Gson
import com.jycra.filmaico.data.channel.data.dao.ChannelDao
import com.jycra.filmaico.data.channel.data.dao.WatchHistoryDao
import com.jycra.filmaico.data.channel.mapper.toDomain
import com.jycra.filmaico.domain.channel.model.Channel
import com.jycra.filmaico.domain.channel.repository.WatchHistoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class WatchHistoryRepositoryImpl @Inject constructor(
    private val channelDao: ChannelDao,
    private val watchHistoryDao: WatchHistoryDao,
    private val gson: Gson
) : WatchHistoryRepository {

    override fun getTopWatchedChannels(): Flow<List<Channel>> {
        return watchHistoryDao.getTopWatchedChannels()
            .map { historyEntities ->
                historyEntities.mapNotNull { historyEntry ->
                    channelDao.getChannelById(historyEntry.channelId)?.toDomain(gson)
                }
            }
    }

    override suspend fun saveWatchTime(channelId: String, watchTimeInSeconds: Long) {
        watchHistoryDao.upsertWatchTime(channelId, watchTimeInSeconds)
    }

}