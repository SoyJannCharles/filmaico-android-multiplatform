package com.jycra.filmaico.domain.media.usecase

import com.jycra.filmaico.domain.history.repository.MediaProgressRepository
import com.jycra.filmaico.domain.media.model.Media
import com.jycra.filmaico.domain.media.model.MediaType
import com.jycra.filmaico.domain.media.repository.MediaRepository
import javax.inject.Inject

class GetMediaAssetUseCase @Inject constructor(
    private val mediaRepository: MediaRepository,
    private val progressRepository: MediaProgressRepository
) {

    suspend operator fun invoke(mediaId: String): Media.Asset? {

        val asset = mediaRepository.getAssetById(mediaId, MediaType.OVA) ?: return null

        val progress = progressRepository.getProgressByMediaId(mediaId)

        return progress?.let {
            asset.copy(
                lastPosition = it.lastPosition,
                isFinished = it.isFinished
            )
        } ?: asset

    }

}