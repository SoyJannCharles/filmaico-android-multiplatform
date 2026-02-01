package com.jycra.filmaico.data.media.repository

import android.util.Log
import com.jycra.filmaico.core.model.media.type.AnimeDto
import com.jycra.filmaico.core.model.media.type.ChannelDto
import com.jycra.filmaico.core.model.media.type.MovieDto
import com.jycra.filmaico.core.model.media.type.SerieDto
import com.jycra.filmaico.core.network.di.ApplicationScope
import com.jycra.filmaico.data.media.data.dao.MediaDao
import com.jycra.filmaico.data.media.data.service.MediaService
import com.jycra.filmaico.data.media.entity.MediaEntity
import com.jycra.filmaico.data.media.entity.MediaSeasonEntity
import com.jycra.filmaico.data.media.util.mapper.dto.toEntity
import com.jycra.filmaico.data.media.util.mapper.dto.toMappingResult
import com.jycra.filmaico.data.media.util.mapper.entity.toAsset
import com.jycra.filmaico.data.media.util.mapper.entity.toDomain
import com.jycra.filmaico.domain.common.content.model.ContentStatus
import com.jycra.filmaico.domain.media.model.Media
import com.jycra.filmaico.domain.media.model.MediaCarousel
import com.jycra.filmaico.domain.media.model.MediaType
import com.jycra.filmaico.domain.media.model.metadata.PlaybackNavigation
import com.jycra.filmaico.domain.media.repository.MediaRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

class MediaRepositoryImpl @Inject constructor(
    private val mediaDao: MediaDao,
    private val mediaService: MediaService,
    @ApplicationScope private val serviceScope: CoroutineScope
) : MediaRepository {

    override suspend fun syncMetadataSnapshot(mediaType: MediaType) {

        coroutineScope {

            mediaService.getCarousels(mediaType).firstOrNull()?.let { dtos ->
                val entities = dtos.map { it.toEntity(mediaType) }
                mediaDao.deleteCarouselsByMediaType(mediaType.value)
                mediaDao.insertCarousels(entities)
            }

            mediaService.getMediaContainers(mediaType).firstOrNull()?.let { snapshots ->
                snapshots.forEach { doc ->
                    val result = when (mediaType) {
                        MediaType.CHANNEL -> doc.toObject(ChannelDto::class.java)?.toMappingResult()
                        MediaType.MOVIE -> doc.toObject(MovieDto::class.java)?.toMappingResult()
                        MediaType.SERIE -> doc.toObject(SerieDto::class.java)?.toMappingResult()
                        MediaType.ANIME -> doc.toObject(AnimeDto::class.java)?.toMappingResult()
                        else -> null
                    }
                    result?.let { mediaDao.upsertMetadata(it.media, it.tags) }
                }
            }

        }

    }

    override suspend fun syncMetadataRealtime(mediaType: MediaType) {

        coroutineScope {

            mediaService.getCarousels(mediaType).firstOrNull()?.let { dtos ->
                val entities = dtos.map { it.toEntity(mediaType) }
                mediaDao.deleteCarouselsByMediaType(mediaType.value)
                mediaDao.insertCarousels(entities)
            }

            mediaService.getMediaContainers(mediaType).firstOrNull()?.let { snapshots ->
                snapshots.forEach { doc ->
                    val result = when (mediaType) {
                        MediaType.CHANNEL -> doc.toObject(ChannelDto::class.java)?.toMappingResult()
                        MediaType.MOVIE -> doc.toObject(MovieDto::class.java)?.toMappingResult()
                        MediaType.SERIE -> doc.toObject(SerieDto::class.java)?.toMappingResult()
                        MediaType.ANIME -> doc.toObject(AnimeDto::class.java)?.toMappingResult()
                        else -> null
                    }
                    result?.let { mediaDao.upsertMetadata(it.media, it.tags) }
                }
            }

        }

    }

    override suspend fun syncMediaContent(containerId: String, mediaType: MediaType) {

        val seasons = mediaService.getMediaSeasons(containerId, mediaType)

        val allMediaEntities = mutableListOf<MediaEntity>()
        val seasonEntities = mutableListOf<MediaSeasonEntity>()

        seasons.forEach { seasonDto ->

            val seasonId = seasonDto.id ?: return@forEach

            seasonEntities.add(
                MediaSeasonEntity(
                    id = seasonId,
                    ownerId = containerId,
                    number = seasonDto.seasonNumber,
                    name = seasonDto.name
                )
            )

            val assets = mediaService.getMediaAssets(containerId, seasonId, mediaType)

            assets.forEach { contentDto ->
                allMediaEntities.add(
                    MediaEntity(
                        id = contentDto.id ?: "",
                        type = MediaType.fromString(contentDto.type).value,
                        ownerType = mediaType.value,
                        seasonId = seasonId,
                        ownerId = containerId,
                        name = contentDto.name,
                        imageUrl = contentDto.thumbnailUrl,
                        duration = contentDto.duration,
                        order = if (contentDto.order != 0) contentDto.order else contentDto.contentNumber,
                        sources = contentDto.sources
                    )
                )
            }

        }

        mediaDao.upsertMediaContent(allMediaEntities, seasonEntities)

    }

    override suspend fun searchAllMedia(query: String): Map<MediaType, List<Media>> {

        if (query.length < 3) return emptyMap()

        val cleanQuery = query.trim()

        return coroutineScope {

            val channels = async { mediaDao.searchByMediaType(cleanQuery, MediaType.CHANNEL.value) }
            val movies = async { mediaDao.searchByMediaType(cleanQuery, MediaType.MOVIE.value) }
            val series = async { mediaDao.searchByMediaType(cleanQuery, MediaType.SERIE.value) }
            val animes = async { mediaDao.searchByMediaType(cleanQuery, MediaType.ANIME.value) }

            buildMap {
                put(MediaType.CHANNEL, channels.await().map { it.toDomain() })
                put(MediaType.MOVIE, movies.await().map { it.toDomain() })
                put(MediaType.SERIE, series.await().map { it.toDomain() })
                put(MediaType.ANIME, animes.await().map { it.toDomain() })
            }.filterValues { it.isNotEmpty() }

        }

    }

    override suspend fun getSavedMediaGrouped(): Flow<Map<MediaType, List<Media>>> {

        return combine(
            mediaDao.getSavedMediaByOwnerType(MediaType.CHANNEL.value),
            mediaDao.getSavedMediaByOwnerType(MediaType.MOVIE.value),
            mediaDao.getSavedMediaByOwnerType(MediaType.SERIE.value),
            mediaDao.getSavedMediaByOwnerType(MediaType.ANIME.value)
        ) { channels, movies, series, animes ->

            buildMap {
                put(MediaType.MOVIE, movies.map { it.toDomain() })
                put(MediaType.SERIE, series.map { it.toDomain() })
                put(MediaType.ANIME, animes.map { it.toDomain() })
                put(MediaType.CHANNEL, channels.map { it.toDomain() })
            }.filterValues { it.isNotEmpty() }

        }

    }

    override suspend fun toggleSaveStatus(ownerId: String, isSaved: Boolean) {
        serviceScope.launch {
            try {
                mediaDao.updateSaveStatus(ownerId, isSaved)
            } catch (e: Exception) {
                Log.e("MediaRepository", "Error al actualizar isSaved para $ownerId", e)
            }
        }.join()
    }

    override fun getCarousels(type: MediaType): Flow<List<MediaCarousel>> {

        return mediaDao.getCarouselsByType(type.value).flatMapLatest { entities ->

            if (entities.isEmpty()) return@flatMapLatest flowOf(emptyList())

            val flows = entities.map { carouselEntity ->
                mediaDao.getMediaByTag(tag = carouselEntity.queryValue, type = type.value)
                    .map { contentEntities ->
                        carouselEntity.toDomain(items = contentEntities.mapNotNull { it.toDomain() })
                    }
            }

            combine(flows) {
                it.toList()
            }

        }

    }

    override fun getContainerById(
        containerId: String,
        mediaType: MediaType
    ): Flow<Media.Container?> {
        return combine(
            mediaDao.getMediaById(id = containerId, type = mediaType.value),
            mediaDao.getSeasonsForMedia(containerId),
            mediaDao.getAssetsByOwnerId(containerId)
        ) { mediaEntity, seasonsEntities, assetsEntities ->

            if (mediaEntity == null) return@combine null

            val seasons = seasonsEntities.map { seasonEntity ->
                val seasonEpisodes = assetsEntities
                    .filter { it.seasonId == seasonEntity.id }
                    .map { it.toAsset() }
                seasonEntity.toDomain(assets = seasonEpisodes)
            }

            Media.Container(
                id = mediaEntity.id,
                name = mediaEntity.name,
                imageUrl = mediaEntity.imageUrl,
                tags = emptyList(),
                mediaType = mediaType,
                ownerMediaType = MediaType.fromString(mediaEntity.ownerType),
                synopsis = mediaEntity.synopsis,
                releaseYear = mediaEntity.releaseYear,
                status = mediaEntity.status?.let { ContentStatus.fromValue(it) }
                    ?: ContentStatus.UNKNOWN,
                seasons = seasons
            )

        }
    }

    override fun getAssetsBySeason(seasonId: String): Flow<List<Media.Asset>> {
        return mediaDao.getAssetsBySeason(seasonId).map { entities ->
            entities.map { it.toAsset() }
        }
    }

    override suspend fun getAssetById(assetId: String, mediaType: MediaType): Media.Asset? {

        val entity = mediaDao.getMediaById(id = assetId, type = mediaType.value)
            .firstOrNull()

        return entity?.toAsset()

    }

    override suspend fun getSiblingsForAsset(
        currentId: String,
        seasonId: String
    ): Pair<String?, String?> {

        val episodes = mediaDao.getAssetsBySeasonSync(seasonId)
        val currentIndex = episodes.indexOfFirst { it.id == currentId }
        val ownerId = mediaDao.getSeasonOwnerId(seasonId) ?: return Pair(null, null)
        val currentSeasonNumber = mediaDao.getSeasonNumber(seasonId) ?: 0

        val nextId = if (currentIndex != -1 && currentIndex < episodes.lastIndex) {
            episodes[currentIndex + 1].id
        } else {
            mediaDao.getNextSeasonId(ownerId, currentSeasonNumber)?.let {
                mediaDao.getAssetsBySeasonSync(it).firstOrNull()?.id
            }
        }

        val prevId = if (currentIndex > 0) {
            episodes[currentIndex - 1].id
        } else {
            mediaDao.getPreviousSeasonId(ownerId, currentSeasonNumber)?.let {
                mediaDao.getAssetsBySeasonSync(it).lastOrNull()?.id
            }
        }

        return Pair(nextId, prevId)

    }

    override suspend fun getPlaybackNavigation(
        ownerId: String,
        currentSeasonId: String,
        currentOrder: Int
    ): PlaybackNavigation {

        val nextAsset =
            mediaDao.getAssetByOrder(currentSeasonId, currentOrder + 1)?.toAsset()
        val prevAsset =
            mediaDao.getAssetByOrder(currentSeasonId, currentOrder - 1)?.toAsset()

        return PlaybackNavigation(
            parentContainerId = ownerId,
            nextAsset = nextAsset,
            prevAsset = prevAsset
        )

    }

}