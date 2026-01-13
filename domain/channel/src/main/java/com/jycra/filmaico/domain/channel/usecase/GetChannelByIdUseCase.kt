package com.jycra.filmaico.domain.channel.usecase

import com.jycra.filmaico.domain.channel.repository.ChannelRepository
import javax.inject.Inject

class GetChannelByIdUseCase @Inject constructor(
    private val channelRepository: ChannelRepository
) {

    suspend operator fun invoke(id: String) =
        channelRepository.getChannelById(id)

}