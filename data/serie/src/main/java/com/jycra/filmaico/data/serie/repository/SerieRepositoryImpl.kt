package com.jycra.filmaico.data.serie.repository

import com.google.gson.Gson
import com.jycra.filmaico.data.serie.data.dao.SerieCarouselDao
import com.jycra.filmaico.data.serie.data.dao.SerieDao
import com.jycra.filmaico.data.serie.data.service.SerieService
import com.jycra.filmaico.data.serie.entity.SerieCarouselEntity
import com.jycra.filmaico.data.serie.entity.SerieEntity
import com.jycra.filmaico.data.serie.mapper.toCrossRefEntities
import com.jycra.filmaico.data.serie.mapper.toDomain
import com.jycra.filmaico.data.serie.mapper.toEntity
import com.jycra.filmaico.data.serie.mapper.toTagEntities
import com.jycra.filmaico.domain.serie.model.Serie
import com.jycra.filmaico.domain.serie.model.SerieCarousel
import com.jycra.filmaico.domain.serie.model.SerieContent
import com.jycra.filmaico.domain.serie.repository.SerieRepository
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

class SerieRepositoryImpl @Inject constructor(
    private val serieService: SerieService,
    private val serieCarouselDao: SerieCarouselDao,
    private val serieDao: SerieDao,
    private val gson: Gson,
    ioDispatcher: CoroutineDispatcher
) : SerieRepository {

    private val scope = CoroutineScope(ioDispatcher + SupervisorJob())

    init {
        syncSerieCarousels()
        syncSeries()
    }

    private fun syncSerieCarousels() {
        serieService.getSerieCarouselsRealtime { remoteSerieCarouselsDto ->
            scope.launch {
                serieCarouselDao.clearAndInsertSerieCarousels(
                    remoteSerieCarouselsDto.map { serieCarouselDto ->
                        serieCarouselDto.toEntity(gson)
                    }
                )
            }
        }
    }

    private fun syncSeries() {

        serieService.getSeriesRealtime { remoteSeriesDto ->

            scope.launch {

                val allSeries = remoteSeriesDto.map { it.toEntity(gson) }
                val allTags = remoteSeriesDto.flatMap { it.toTagEntities() }.distinctBy { it.tagName }
                val allCrossRefs = remoteSeriesDto.flatMap { it.toCrossRefEntities() }

                serieDao.syncSeriesList(
                    series = allSeries,
                    tags = allTags,
                    crossRefs = allCrossRefs
                )

            }

        }

    }

    override fun syncSerieContent(serieOwnerId: String) {
        serieService.getSerieContentRealtime(
            serieId = serieOwnerId,
            onSeasonsUpdate = { seasons ->
                scope.launch {
                    serieDao.insertSerieSeasons(seasons.map { it.toEntity(gson, serieOwnerId) })
                }
            },
            onContentUpdate = { seasonOwnerId, content ->
                scope.launch {
                    serieDao.insertSerieContent(content.map { it.toEntity(gson, seasonOwnerId) })
                }
            }
        )
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getSerieCarousels(): Flow<List<SerieCarousel>> {
        return serieCarouselDao.getSerieCarousels().flatMapLatest { serieCarouselEntities ->
            if (serieCarouselEntities.isEmpty())
                return@flatMapLatest flowOf(emptyList())
            val carouselFlows = serieCarouselEntities.map { serieCarouselEntity ->
                fetchMoviesForCarousel(serieCarouselEntity)
            }
            combine(carouselFlows) {
                    movieCarousels -> movieCarousels.toList()
            }
        }
    }

    override fun getSerieById(id: String): Flow<Serie?> {

        return serieDao.getSerieWithDetailsById(id).map { serieWithDetails ->

            if (serieWithDetails == null) {
                return@map null
            }

            val sortedSeasons = serieWithDetails.seasons.sortedBy { it.season.seasonNumber }

            val seasonsWithSortedContent = sortedSeasons.map { seasonWithContent ->
                val sortedContent = seasonWithContent.content.sortedBy { it.order }
                seasonWithContent.copy(content = sortedContent)
            }

            serieWithDetails.copy(seasons = seasonsWithSortedContent)
                .toDomain(gson)

        }

    }

    override suspend fun getSerieContentById(contentId: String): SerieContent? =
        serieDao.getSerieContentById(contentId)?.toDomain(gson)

    private fun fetchMoviesForCarousel(serieCarouselEntity: SerieCarouselEntity): Flow<SerieCarousel> {

        val seriesFlow: Flow<List<SerieEntity>> = when (serieCarouselEntity.queryType) {
            "tag" -> {
                serieDao.getSeriesByTag(
                    gson.fromJson(serieCarouselEntity.queryValueJson, String::class.java)
                )
            }
            else -> flowOf(emptyList())
        }

        return seriesFlow.map { serieEntities ->
            serieCarouselEntity.toDomain(gson, serieEntities)
        }

    }

}