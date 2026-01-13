package com.jycra.filmaico.domain.serie.usecase

import com.jycra.filmaico.domain.serie.model.SerieContent
import com.jycra.filmaico.domain.serie.repository.SerieRepository
import javax.inject.Inject

class GetSerieContentByIdUseCase @Inject constructor(
    private val serieRepository: SerieRepository
) {

    suspend operator fun invoke(contentId: String): SerieContent? =
        serieRepository.getSerieContentById(contentId)

}