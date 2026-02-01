package com.jycra.filmaico.domain.media.usecase

import com.jycra.filmaico.domain.media.model.MediaType
import com.jycra.filmaico.domain.media.repository.MediaRepository
import javax.inject.Inject

class SyncMediaContentUseCase @Inject constructor(
    private val repository: MediaRepository
) {

    suspend operator fun invoke(containerId: String, mediaType: MediaType) {
        repository.syncMediaContent(containerId, mediaType)
    }

}