package com.jycra.filmaico.domain.anime.repository

import com.jycra.filmaico.domain.anime.model.Anime
import com.jycra.filmaico.domain.anime.model.AnimeCarousel
import com.jycra.filmaico.domain.anime.model.AnimeContent
import kotlinx.coroutines.flow.Flow

interface AnimeRepository {

    fun syncAnimeContent(animeOwnerId: String)

    fun getAnimeCarousels(): Flow<List<AnimeCarousel>>

    fun getAnimeById(id: String): Flow<Anime?>
    suspend fun getAnimeContentById(contentId: String): AnimeContent?

}