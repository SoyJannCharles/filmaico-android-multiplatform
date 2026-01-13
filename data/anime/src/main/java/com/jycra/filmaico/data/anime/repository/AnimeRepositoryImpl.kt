package com.jycra.filmaico.data.anime.repository

import com.google.gson.Gson
import com.jycra.filmaico.data.anime.data.dao.AnimeCarouselDao
import com.jycra.filmaico.data.anime.data.dao.AnimeDao
import com.jycra.filmaico.data.anime.data.service.AnimeService
import com.jycra.filmaico.data.anime.entity.AnimeCarouselEntity
import com.jycra.filmaico.data.anime.entity.AnimeEntity
import com.jycra.filmaico.data.anime.mapper.toCrossRefEntities
import com.jycra.filmaico.data.anime.mapper.toDomain
import com.jycra.filmaico.data.anime.mapper.toEntity
import com.jycra.filmaico.data.anime.mapper.toTagEntities
import com.jycra.filmaico.domain.anime.model.Anime
import com.jycra.filmaico.domain.anime.model.AnimeCarousel
import com.jycra.filmaico.domain.anime.model.AnimeContent
import com.jycra.filmaico.domain.anime.repository.AnimeRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.collections.map

class AnimeRepositoryImpl @Inject constructor(
    private val animeService: AnimeService,
    private val animeCarouselDao: AnimeCarouselDao,
    private val animeDao: AnimeDao,
    private val gson: Gson,
    ioDispatcher: CoroutineDispatcher
) : AnimeRepository {

    private val scope = CoroutineScope(ioDispatcher + SupervisorJob())

    init {
        syncAnimeCarousels()
        syncAnimes()
    }

    private fun syncAnimeCarousels() {
        animeService.getAnimeCarouselsRealtime { remoteAnimeCarouselsDto ->
            scope.launch {
                animeCarouselDao.clearAndInsertAnimeCarousels(
                    remoteAnimeCarouselsDto.map { animeCarouselDto ->
                        animeCarouselDto.toEntity(gson)
                    }
                )
            }
        }
    }

    private fun syncAnimes() {

        animeService.getAnimesRealtime { remoteAnimesDto ->

            scope.launch {

                val allAnimes = remoteAnimesDto.map { it.toEntity(gson) }
                val allTags = remoteAnimesDto.flatMap { it.toTagEntities() }.distinctBy { it.tagName }
                val allCrossRefs = remoteAnimesDto.flatMap { it.toCrossRefEntities() }

                animeDao.syncAnimesList(
                    animes = allAnimes,
                    tags = allTags,
                    crossRefs = allCrossRefs
                )

            }

        }

    }

    override fun syncAnimeContent(animeOwnerId: String) {
        animeService.getAnimeContentRealtime(
            animeId = animeOwnerId,
            onSeasonsUpdate = { seasons ->
                scope.launch {
                    animeDao.insertAnimeSeasons(seasons.map { it.toEntity(gson, animeOwnerId) })
                }
            },
            onContentUpdate = { seasonOwnerId, content ->
                scope.launch {
                    animeDao.insertAnimeContent(content.map { it.toEntity(gson, seasonOwnerId) })
                }
            }
        )
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getAnimeCarousels(): Flow<List<AnimeCarousel>> {
        return animeCarouselDao.getAnimeCarousels().flatMapLatest { animeCarouselEntities ->
            if (animeCarouselEntities.isEmpty())
                return@flatMapLatest flowOf(emptyList())
            val carouselFlows = animeCarouselEntities.map { animeCarouselEntity ->
                fetchAnimesForCarousel(animeCarouselEntity)
            }
            combine(carouselFlows) {
                    animeCarousels -> animeCarousels.toList()
            }
        }
    }

    override fun getAnimeById(id: String): Flow<Anime?> {
        return animeDao.getAnimeWithDetailById(id).map { animeWithDetails ->
            animeWithDetails?.toDomain(gson)
        }
    }

    override suspend fun getAnimeContentById(contentId: String): AnimeContent? =
        animeDao.getAnimeContentById(contentId)?.toDomain(gson)

    private fun fetchAnimesForCarousel(animeCarouselEntity: AnimeCarouselEntity): Flow<AnimeCarousel> {

        val animesFlow: Flow<List<AnimeEntity>> = when (animeCarouselEntity.queryType) {
            "tag" -> {
                animeDao.getAnimesByTag(
                    gson.fromJson(animeCarouselEntity.queryValueJson, String::class.java)
                )
            }
            else -> flowOf(emptyList())
        }

        return animesFlow.map { animeEntities ->
            animeCarouselEntity.toDomain(gson, animeEntities)
        }

    }

}