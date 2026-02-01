package com.jycra.filmaico.domain.media.usecase

import com.jycra.filmaico.domain.history.repository.MediaProgressRepository
import com.jycra.filmaico.domain.media.model.MediaCarousel
import com.jycra.filmaico.domain.media.model.MediaType
import com.jycra.filmaico.domain.media.repository.MediaRepository
import com.jycra.filmaico.domain.media.util.mapper.toMediaAsset
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

class GetMediaContentUseCase @Inject constructor(
    private val repository: MediaRepository,
    private val progressRepository: MediaProgressRepository
) {

    operator fun invoke(mediaType: MediaType): Flow<List<MediaCarousel>> {

        return combine(
            repository.getCarousels(mediaType),
            progressRepository.getRecentProgressByOwnerType(ownerMediaType = mediaType.value)
        ) { remoteCarousels, progressList ->


            val recentItems = progressList
                .filter { !it.isFinished }
                .map { it.toMediaAsset() }

            val mediaContent = mutableListOf<MediaCarousel>()

            if (recentItems.isNotEmpty()) {
                mediaContent.add(
                    MediaCarousel(
                        id = "continue_watching",
                        title = mapOf(
                            "es" to "Continuar viendo",
                            "en" to "Continue watching"
                        ),
                        type = mediaType,
                        items = recentItems
                    )
                )
            }

            mediaContent.addAll(remoteCarousels)

            mediaContent.filter { it.items.isNotEmpty() }

        }

    }

}