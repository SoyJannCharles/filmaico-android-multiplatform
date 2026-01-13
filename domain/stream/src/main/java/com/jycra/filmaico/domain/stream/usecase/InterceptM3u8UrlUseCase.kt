package com.jycra.filmaico.domain.stream.usecase

import com.jycra.filmaico.domain.stream.repository.MediaUrlRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class InterceptM3u8UrlUseCase @Inject constructor(
    private val mediaUrlRepository: MediaUrlRepository
) {

    operator fun invoke(url: String): Flow<String> =
        mediaUrlRepository.getAnimeUrl(url)

}