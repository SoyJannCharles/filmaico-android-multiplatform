package com.jycra.filmaico.domain.serie.usecase

import com.jycra.filmaico.domain.serie.model.Serie
import com.jycra.filmaico.domain.serie.repository.SerieRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetSerieByIdUseCase @Inject constructor(
    private val serieRepository: SerieRepository
) {

    operator fun invoke(id: String): Flow<Serie?> =
        serieRepository.getSerieById(id)

}