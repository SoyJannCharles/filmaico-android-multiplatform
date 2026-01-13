package com.jycra.filmaico.domain.channel.usecase

import com.jycra.filmaico.domain.channel.repository.WatchHistoryRepository
import javax.inject.Inject

class SaveWatchHistoryUseCase @Inject constructor(
    private val watchHistoryRepository: WatchHistoryRepository
) {

    suspend operator fun invoke(channelId: String, watchTimeInSeconds: Long) {
        watchHistoryRepository.saveWatchTime(channelId, watchTimeInSeconds)
    }

}