package com.jycra.filmaico.domain.anime.usecase

import com.jycra.filmaico.domain.anime.model.Anime
import com.jycra.filmaico.domain.anime.repository.AnimeRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAnimeByIdUseCase @Inject constructor(
    private val animeRepository: AnimeRepository
) {

    operator fun invoke(id: String): Flow<Anime?> =
        animeRepository.getAnimeById(id)

}