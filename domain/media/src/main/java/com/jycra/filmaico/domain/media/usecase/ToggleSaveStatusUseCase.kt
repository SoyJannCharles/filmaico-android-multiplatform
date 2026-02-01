package com.jycra.filmaico.domain.media.usecase

import com.jycra.filmaico.domain.media.repository.MediaRepository
import javax.inject.Inject

class ToggleSaveStatusUseCase @Inject constructor(
    private val repository: MediaRepository
) {

    suspend operator fun invoke(ownerId: String, isSaved: Boolean) {
        repository.toggleSaveStatus(ownerId, isSaved)
    }

}