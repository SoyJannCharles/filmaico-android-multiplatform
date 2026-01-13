package com.jycra.filmaico.data.channel.repository

import com.google.gson.Gson
import com.jycra.filmaico.data.channel.data.dao.ChannelCarouselDao
import com.jycra.filmaico.data.channel.data.dao.ChannelDao
import com.jycra.filmaico.data.channel.data.service.ChannelService
import com.jycra.filmaico.data.channel.entity.ChannelCarouselEntity
import com.jycra.filmaico.data.channel.entity.ChannelEntity
import com.jycra.filmaico.data.channel.mapper.toDomain
import com.jycra.filmaico.data.channel.mapper.toEntity
import com.jycra.filmaico.domain.channel.repository.ChannelRepository
import com.jycra.filmaico.domain.channel.model.Channel
import com.jycra.filmaico.domain.channel.model.ChannelCarousel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

class ChannelRepositoryImpl @Inject constructor(
    private val channelService: ChannelService,
    private val channelCarouselDao: ChannelCarouselDao,
    private val channelDao: ChannelDao,
    private val gson: Gson,
    ioDispatcher: CoroutineDispatcher
) : ChannelRepository {

    private val scope = CoroutineScope(ioDispatcher + SupervisorJob())

    init {
        syncChannels()
        syncChannelCarousels()
    }

    private fun syncChannels() {
        channelService.getChannelsRealtime { remoteChannelsDto ->
            scope.launch {
                channelDao.clearAndInsertChannels(
                    remoteChannelsDto.map { channelDto ->
                        channelDto.toEntity(gson)
                    }
                )
            }
        }
    }

    private fun syncChannelCarousels() {
        channelService.getChannelCarouselsRealtime { remoteChannelCarouselsDto ->
            scope.launch {
                channelCarouselDao.clearAndInsertChannelCarousels(
                    remoteChannelCarouselsDto.map { channelCarouselDto ->
                        channelCarouselDto.toEntity(gson)
                    }
                )
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getChannelCarousels(): Flow<List<ChannelCarousel>> {
        return channelCarouselDao.getChannelCarousels().flatMapLatest { channelCarouselEntities ->
            if (channelCarouselEntities.isEmpty())
                return@flatMapLatest flowOf(emptyList())
            val carouselFlows = channelCarouselEntities.map { channelCarouselEntity ->
                fetchChannelsForCarousel(channelCarouselEntity)
            }
            combine(carouselFlows) {
                channelCarousels -> channelCarousels.toList()
            }
        }
    }

    override suspend fun getChannelById(id: String): Channel? =
        channelDao.getChannelById(id)?.toDomain(gson)

    private fun fetchChannelsForCarousel(channelCarouselEntity: ChannelCarouselEntity): Flow<ChannelCarousel> {

        val channelsFlow: Flow<List<ChannelEntity>> = when (channelCarouselEntity.queryType) {
            "tag" -> {
                channelDao.getChannelsByTag(
                    gson.fromJson(channelCarouselEntity.queryValueJson, String::class.java)
                )
            }
            else -> flowOf(emptyList())
        }

        return channelsFlow.map { animeEntities ->
            channelCarouselEntity.toDomain(gson, animeEntities)
        }

    }

}