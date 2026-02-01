package com.jycra.filmaico.domain.media.usecase

import com.jycra.filmaico.domain.media.model.MediaType
import com.jycra.filmaico.domain.media.repository.MediaRepository
import javax.inject.Inject

class SyncMetadataSnapshotUseCase @Inject constructor(
    private val repository: MediaRepository
) {

    suspend operator fun invoke(mediaType: MediaType) {
        repository.syncMetadataSnapshot(mediaType)
    }

}