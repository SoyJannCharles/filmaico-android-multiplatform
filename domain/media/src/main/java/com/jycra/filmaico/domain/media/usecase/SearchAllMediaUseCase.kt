package com.jycra.filmaico.domain.media.usecase

import com.jycra.filmaico.domain.media.model.Media
import com.jycra.filmaico.domain.media.model.MediaType
import com.jycra.filmaico.domain.media.repository.MediaRepository
import javax.inject.Inject

class SearchAllMediaUseCase @Inject constructor(
    private val repository: MediaRepository
) {

    suspend operator fun invoke(query: String): Map<MediaType, List<Media>> =
        repository.searchAllMedia(query)

}