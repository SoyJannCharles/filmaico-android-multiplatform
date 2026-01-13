package com.jycra.filmaico.domain.anime.usecase

import com.jycra.filmaico.domain.anime.repository.AnimeRepository
import javax.inject.Inject

class SyncAnimeContentUseCase @Inject constructor(
    private val animeRepository: AnimeRepository
) {

    operator fun invoke(animeId: String) =
        animeRepository.syncAnimeContent(animeId)

}