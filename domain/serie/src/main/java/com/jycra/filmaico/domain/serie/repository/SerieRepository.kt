package com.jycra.filmaico.domain.serie.repository

import com.jycra.filmaico.domain.serie.model.Serie
import com.jycra.filmaico.domain.serie.model.SerieCarousel
import com.jycra.filmaico.domain.serie.model.SerieContent
import kotlinx.coroutines.flow.Flow

interface SerieRepository {

    fun syncSerieContent(serieOwnerId: String)

    fun getSerieCarousels(): Flow<List<SerieCarousel>>

    fun getSerieById(id: String): Flow<Serie?>
    suspend fun getSerieContentById(contentId: String): SerieContent?

}