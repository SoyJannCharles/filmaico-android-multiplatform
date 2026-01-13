package com.jycra.filmaico.domain.serie.usecase

import com.jycra.filmaico.domain.serie.model.SerieCarousel
import com.jycra.filmaico.domain.serie.repository.SerieRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetSerieContentUseCase @Inject constructor(
    private val serieRepository: SerieRepository
) {

    operator fun invoke(): Flow<List<SerieCarousel>> {
        return serieRepository.getSerieCarousels()
    }

}