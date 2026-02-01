package com.jycra.filmaico.domain.media.usecase

import com.jycra.filmaico.domain.history.repository.MediaProgressRepository
import com.jycra.filmaico.domain.media.model.Media
import com.jycra.filmaico.domain.media.model.MediaType
import com.jycra.filmaico.domain.media.repository.MediaRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

class GetMediaContainerUseCase @Inject constructor(
    private val repository: MediaRepository,
    private val progressRepository: MediaProgressRepository
) {

    operator fun invoke(containerId: String, mediaType: MediaType): Flow<Media.Container?> {

        return combine(
            repository.getContainerById(containerId, mediaType),
            progressRepository.getProgressByOwnerId(containerId)
        ) { container, localProgress ->

            if (container == null) return@combine null

            val progressMap = localProgress.associateBy { it.mediaId }

            container.copy(
                seasons = container.seasons.map { season ->
                    season.copy(
                        episodes = season.episodes.map { episode ->
                            val progress = progressMap[episode.id]
                            episode.copy(
                                lastPosition = progress?.lastPosition ?: 0L,
                                isFinished = progress?.isFinished ?: false
                            )
                        }
                    )
                }
            )

        }

    }

}