package com.jycra.filmaico.domain.media.usecase

import com.jycra.filmaico.domain.media.repository.MediaRepository
import javax.inject.Inject

class SyncEpgUseCase @Inject constructor(
    private val repository: MediaRepository
) {

    suspend operator fun invoke() =
        repository.syncEpg()

}