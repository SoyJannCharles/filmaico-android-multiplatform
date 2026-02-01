package com.jycra.filmaico.domain.media.repository

import com.jycra.filmaico.domain.media.model.metadata.PlaybackNavigation
import com.jycra.filmaico.domain.media.model.Media
import com.jycra.filmaico.domain.media.model.MediaCarousel
import com.jycra.filmaico.domain.media.model.MediaType
import kotlinx.coroutines.flow.Flow

interface MediaRepository {

    suspend fun syncMetadataSnapshot(mediaType: MediaType)
    suspend fun syncMetadataRealtime(mediaType: MediaType)
    suspend fun syncMediaContent(containerId: String, mediaType: MediaType)

    suspend fun searchAllMedia(query: String): Map<MediaType, List<Media>>

    suspend fun getSavedMediaGrouped(): Flow<Map<MediaType, List<Media>>>
    suspend fun toggleSaveStatus(ownerId: String, isSaved: Boolean)

    fun getCarousels(type: MediaType): Flow<List<MediaCarousel>>

    fun getContainerById(containerId: String, mediaType: MediaType): Flow<Media.Container?>

    fun getAssetsBySeason(seasonId: String): Flow<List<Media.Asset>>
    suspend fun getSiblingsForAsset(currentId: String, seasonId: String): Pair<String?, String?>

    suspend fun getAssetById(assetId: String, mediaType: MediaType): Media.Asset?

    suspend fun getPlaybackNavigation(
        ownerId: String,
        currentSeasonId: String,
        currentOrder: Int
    ): PlaybackNavigation

}