package com.jycra.filmaico.domain.media.usecase

import com.jycra.filmaico.domain.media.model.MediaCarousel
import com.jycra.filmaico.domain.media.model.MediaType
import com.jycra.filmaico.domain.media.repository.MediaRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetSavedMediaUseCase @Inject constructor(
    private val repository: MediaRepository
) {

    suspend operator fun invoke(): Flow<List<MediaCarousel>> {
        return repository.getSavedMediaGrouped().map { groupedMedia ->
            groupedMedia.map { (type, items) ->
                MediaCarousel(
                    id = type.value,
                    title = when (type) {
                        MediaType.CHANNEL -> mapOf("es" to "Canales")
                        MediaType.MOVIE -> mapOf("es" to "Peliculas")
                        MediaType.SERIE -> mapOf("es" to "Series")
                        MediaType.ANIME -> mapOf("es" to "Animes")
                        else -> mapOf("es" to "Guardados")
                    },
                    items = items,
                    type = type
                )
            }
        }
    }

}