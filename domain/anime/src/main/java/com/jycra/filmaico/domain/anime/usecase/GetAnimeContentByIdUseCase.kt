package com.jycra.filmaico.domain.anime.usecase

import com.jycra.filmaico.domain.anime.model.AnimeContent
import com.jycra.filmaico.domain.anime.repository.AnimeRepository
import javax.inject.Inject

class GetAnimeContentByIdUseCase @Inject constructor(
    private val animeRepository: AnimeRepository
) {

    suspend operator fun invoke(contentId: String) : AnimeContent? =
        animeRepository.getAnimeContentById(contentId)

}