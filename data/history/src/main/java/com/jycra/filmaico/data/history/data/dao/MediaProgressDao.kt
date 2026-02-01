package com.jycra.filmaico.data.history.data.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.jycra.filmaico.data.history.entity.MediaProgressEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MediaProgressDao {

    @Upsert
    suspend fun upsertProgress(entry: MediaProgressEntity)

    @Query(
        """
        SELECT * FROM media_progress 
        GROUP BY CASE WHEN seasonId IS NULL THEN mediaId ELSE seasonId END
        ORDER BY lastWatchedMillis DESC 
        LIMIT 20
    """
    )
    fun getRecentProgress(): Flow<List<MediaProgressEntity>>

    @Query(
        """
        SELECT mp.*
        FROM media_progress mp
        INNER JOIN (
            SELECT ownerId, MAX(lastWatchedMillis) as maxTime
            FROM media_progress
            WHERE ownerMediaType = :ownerMediaType
            GROUP BY ownerId
        ) latest 
        ON mp.ownerId = latest.ownerId 
        AND mp.lastWatchedMillis = latest.maxTime
        WHERE mp.ownerMediaType = :ownerMediaType
        ORDER BY mp.lastWatchedMillis DESC
        LIMIT :limit
        """
    )
    fun getRecentProgressByOwnerMediaType(ownerMediaType: String, limit: Int = 20): Flow<List<MediaProgressEntity>>

    @Query("SELECT * FROM media_progress WHERE ownerId = :ownerId")
    fun getProgressByOwnerFlow(ownerId: String): Flow<List<MediaProgressEntity>>

    @Query("SELECT * FROM media_progress WHERE mediaId = :mediaId")
    suspend fun getProgressByMediaId(mediaId: String): MediaProgressEntity?

    @Query("DELETE FROM media_progress WHERE mediaId = :contentId")
    suspend fun deleteById(contentId: String)

    @Query("DELETE FROM media_progress")
    suspend fun clearAll()

}