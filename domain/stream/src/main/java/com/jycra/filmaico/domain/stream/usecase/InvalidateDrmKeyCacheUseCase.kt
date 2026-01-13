package com.jycra.filmaico.domain.stream.usecase

import com.jycra.filmaico.domain.stream.repository.AttrStreamRepository
import javax.inject.Inject

class InvalidateDrmKeyCacheUseCase @Inject constructor(
    private val attrStreamRepository: AttrStreamRepository
) {

    suspend operator fun invoke(channelId: String) {
        attrStreamRepository.invalidateDrmKeyCache(channelId)
    }

}