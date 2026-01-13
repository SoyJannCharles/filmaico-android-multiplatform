package com.jycra.filmaico.domain.stream.usecase

import com.jycra.filmaico.domain.stream.repository.StreamProcessingRepository
import javax.inject.Inject

class InvalidateStreamUrlCacheUseCase @Inject constructor(
    private val streamProcessingRepository: StreamProcessingRepository
) {

    suspend operator fun invoke(contentId: String, contentType: String) {
        streamProcessingRepository.saveUrlToCache(contentId, contentType, null)
    }

}