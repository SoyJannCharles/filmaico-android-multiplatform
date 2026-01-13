package com.jycra.filmaico.domain.channel.usecase

import com.jycra.filmaico.domain.channel.model.ChannelCarousel
import com.jycra.filmaico.domain.channel.repository.ChannelRepository
import com.jycra.filmaico.domain.channel.repository.WatchHistoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

class GetChannelContentUseCase @Inject constructor(
    private val channelRepository: ChannelRepository,
    private val watchHistoryRepository: WatchHistoryRepository
) {

    operator fun invoke(): Flow<List<ChannelCarousel>> {
        return watchHistoryRepository.getTopWatchedChannels().combine(
            channelRepository.getChannelCarousels()
        ) { topWatched, remoteCarousels ->

            val allCarousels = mutableListOf<ChannelCarousel>()

            if (topWatched.isNotEmpty()) {

            }

            remoteCarousels.map { carousel ->
                if (carousel.channels.isNotEmpty()) {
                    allCarousels.add(carousel)
                }
            }

            allCarousels

        }
    }

}