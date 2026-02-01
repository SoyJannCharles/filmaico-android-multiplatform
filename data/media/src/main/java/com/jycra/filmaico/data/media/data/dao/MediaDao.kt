package com.jycra.filmaico.data.media.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.jycra.filmaico.core.model.stream.StreamDto
import com.jycra.filmaico.data.media.entity.MediaCarouselEntity
import com.jycra.filmaico.data.media.entity.MediaEntity
import com.jycra.filmaico.data.media.entity.MediaSeasonEntity
import com.jycra.filmaico.data.media.entity.MediaTagCrossRef
import kotlinx.coroutines.flow.Flow

@Dao
interface MediaDao {

    @Query("""
        SELECT DISTINCT m.* FROM media m
        LEFT JOIN media_tag_cross_ref t ON m.id = t.mediaId
        WHERE m.type = :mediaType
        AND (
            m.name LIKE '%' || :query || '%' 
            OR m.synopsis LIKE '%' || :query || '%'
            OR t.tag LIKE '%' || :query || '%'
        )
        LIMIT 20
    """)
    suspend fun searchByMediaType(query: String, mediaType: String): List<MediaEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCarousels(carousels: List<MediaCarouselEntity>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertMediaIgnore(media: List<MediaEntity>): List<Long>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMediaTagCrossRefs(crossRefs: List<MediaTagCrossRef>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSeasons(seasons: List<MediaSeasonEntity>)

    @Query("""
        UPDATE media SET 
        name = :name, 
        imageUrl = :imageUrl, 
        sources = :sources, 
        synopsis = :synopsis,
        releaseYear = :releaseYear,
        status = :status,
        duration = :duration,
        `order` = :order
        WHERE id = :id
    """)
    suspend fun updateMediaSelective(
        id: String,
        name: Map<String, String>,
        imageUrl: String,
        sources: List<StreamDto>,
        synopsis: Map<String, String>,
        releaseYear: Int?,
        status: String?,
        duration: Long?,
        order: Int?
    )

    @Transaction
    suspend fun upsertMetadata(
        media: List<MediaEntity>,
        tags: List<MediaTagCrossRef>,
        seasons: List<MediaSeasonEntity> = emptyList()
    ) {

        media.forEach { item ->
            val id = insertMediaIgnore(listOf(item)).first()
            if (id == -1L) {
                updateMediaSelective(
                    id = item.id,
                    name = item.name,
                    imageUrl = item.imageUrl,
                    synopsis = item.synopsis,
                    releaseYear = item.releaseYear,
                    status = item.status,
                    duration = item.duration,
                    order = item.order,
                    sources = item.sources,
                )
            }
        }

        insertMediaTagCrossRefs(tags)

    }

    @Transaction
    suspend fun upsertMediaContent(
        media: List<MediaEntity>,
        seasons: List<MediaSeasonEntity>
    ) {

        insertSeasons(seasons)

        media.forEach { episode ->
            val id = insertMediaIgnore(listOf(episode)).first()
            if (id == -1L) {
                updateMediaSelective(
                    id = episode.id,
                    name = episode.name,
                    imageUrl = episode.imageUrl,
                    synopsis = episode.synopsis,
                    releaseYear = episode.releaseYear,
                    status = episode.status,
                    duration = episode.duration,
                    order = episode.order,
                    sources = episode.sources
                )
            }
        }

    }

    @Query("SELECT * FROM media WHERE isSaved = 1 AND ownerType = :type")
    fun getSavedMediaByOwnerType(type: String): Flow<List<MediaEntity>>

    @Query("SELECT * FROM media_carousels WHERE type = :type ORDER BY `order` ASC")
    fun getCarouselsByType(type: String): Flow<List<MediaCarouselEntity>>

    @Query("""
        SELECT m.* FROM media m
        INNER JOIN media_tag_cross_ref xr ON m.id = xr.mediaId
        WHERE xr.tag = :tag 
        AND m.type = :type
        ORDER BY m.name ASC
    """)
    fun getMediaByTag(tag: String, type: String): Flow<List<MediaEntity>>

    @Query("SELECT * FROM media WHERE id = :id AND type = :type")
    fun getMediaById(id: String, type: String): Flow<MediaEntity?>

    @Query("SELECT * FROM media_seasons WHERE ownerId = :ownerId ORDER BY number ASC")
    fun getSeasonsForMedia(ownerId: String): Flow<List<MediaSeasonEntity>>

    @Query("""
        SELECT id FROM media_seasons 
        WHERE ownerId = :ownerId AND number > :currentSeasonNumber 
        ORDER BY number ASC LIMIT 1
    """)
    suspend fun getNextSeasonId(ownerId: String, currentSeasonNumber: Int): String?

    @Query("""
        SELECT id FROM media_seasons 
        WHERE ownerId = :ownerId AND number < :currentSeasonNumber 
        ORDER BY number DESC LIMIT 1
    """)
    suspend fun getPreviousSeasonId(ownerId: String, currentSeasonNumber: Int): String?

    @Query("SELECT ownerId FROM media_seasons WHERE id = :seasonId")
    suspend fun getSeasonOwnerId(seasonId: String): String?

    @Query("SELECT number FROM media_seasons WHERE id = :seasonId")
    suspend fun getSeasonNumber(seasonId: String): Int?

    @Query("SELECT * FROM media WHERE ownerId = :ownerId AND seasonId IS NOT NULL ORDER BY `order` ASC")
    fun getAssetsByOwnerId(ownerId: String): Flow<List<MediaEntity>>

    @Query("SELECT * FROM media WHERE seasonId = :seasonId ORDER BY `order` ASC")
    suspend fun getAssetsBySeasonSync(seasonId: String): List<MediaEntity>

    @Query("SELECT * FROM media WHERE seasonId = :seasonId ORDER BY `order` ASC")
    fun getAssetsBySeason(seasonId: String): Flow<List<MediaEntity>>

    @Query("""
        SELECT * FROM media 
        WHERE seasonId = :seasonId AND `order` = :order 
        LIMIT 1
    """)
    suspend fun getAssetByOrder(seasonId: String, order: Int): MediaEntity?

    @Query("UPDATE media SET isSaved = :isSaved WHERE id = :ownerId")
    suspend fun updateSaveStatus(ownerId: String, isSaved: Boolean)

    @Query("DELETE FROM media WHERE type = :type")
    suspend fun deleteMediaByType(type: String)

    @Query("DELETE FROM media_carousels WHERE type = :mediaType")
    suspend fun deleteCarouselsByMediaType(mediaType: String)

}