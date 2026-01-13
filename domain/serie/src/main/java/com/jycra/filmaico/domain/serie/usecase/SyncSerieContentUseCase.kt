package com.jycra.filmaico.domain.serie.usecase

import com.jycra.filmaico.domain.serie.repository.SerieRepository
import javax.inject.Inject

class SyncSerieContentUseCase @Inject constructor(
    private val serieRepository: SerieRepository
) {

    operator fun invoke(serieOwnerId: String) {
        serieRepository.syncSerieContent(serieOwnerId)
    }

}