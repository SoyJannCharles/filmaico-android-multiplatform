package com.jycra.filmaico.domain.anime.usecase

import com.jycra.filmaico.domain.anime.model.AnimeCarousel
import com.jycra.filmaico.domain.anime.repository.AnimeRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAnimeContentUseCase @Inject constructor(
    private val animeRepository: AnimeRepository
) {

    operator fun invoke(): Flow<List<AnimeCarousel>> {
        return animeRepository.getAnimeCarousels()
    }

}