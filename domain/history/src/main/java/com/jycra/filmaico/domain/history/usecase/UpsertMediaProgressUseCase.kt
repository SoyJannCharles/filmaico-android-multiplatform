package com.jycra.filmaico.domain.history.usecase

import com.jycra.filmaico.domain.history.model.MediaProgress
import com.jycra.filmaico.domain.history.repository.MediaProgressRepository
import javax.inject.Inject

class UpsertMediaProgressUseCase @Inject constructor(
    private val repository: MediaProgressRepository
) {

    suspend operator fun invoke(progress: MediaProgress) {
        repository.upsertProgress(progress)
    }

}