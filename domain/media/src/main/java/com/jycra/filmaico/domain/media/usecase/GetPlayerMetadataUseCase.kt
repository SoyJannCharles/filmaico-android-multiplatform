package com.jycra.filmaico.domain.media.usecase

import com.jycra.filmaico.domain.history.repository.MediaProgressRepository
import com.jycra.filmaico.domain.media.model.MediaType
import com.jycra.filmaico.domain.media.model.metadata.PlayerMetadata
import com.jycra.filmaico.domain.media.repository.MediaRepository
import com.jycra.filmaico.domain.media.util.extesion.localizedImageUrl
import javax.inject.Inject

class GetPlayerMetadataUseCase @Inject constructor(
    private val mediaRepository: MediaRepository,
    private val progressRepository: MediaProgressRepository
) {

    suspend operator fun invoke(assetId: String, mediaType: MediaType): PlayerMetadata? {

        val asset = mediaRepository.getAssetById(assetId = assetId, mediaType = mediaType) ?: return null
        val progress = progressRepository.getProgressByMediaId(assetId)

        val siblings = if (asset.seasonId != null) {
            mediaRepository.getSiblingsForAsset(asset.id, asset.seasonId)
        } else {
            Pair(null, null)
        }

        return PlayerMetadata(
            assetId = asset.id,
            seasonId = asset.seasonId,
            ownerId = asset.ownerId ?: asset.id,
            mediaType = asset.mediaType,
            ownerMediaType = asset.ownerMediaType,
            name = asset.name,
            imageUrl = asset.localizedImageUrl,
            isSaved = asset.isSaved,
            order = asset.number ?: 0,
            duration = asset.duration?.let { it * 60000L } ?: 0,
            lastPosition = progress?.lastPosition ?: 0L,
            isFinished = progress?.isFinished ?: false,
            isLive = asset.isLive,
            nextContentId = siblings.first,
            prevContentId = siblings.second,
            sources = asset.sources,
        )

    }

}