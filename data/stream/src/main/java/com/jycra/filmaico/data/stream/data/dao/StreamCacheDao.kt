package com.jycra.filmaico.data.stream.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.jycra.filmaico.data.stream.entity.StreamCacheEntity
import com.jycra.filmaico.domain.media.model.MediaType
import com.jycra.filmaico.domain.media.model.stream.DrmKeys

@Dao
interface StreamCacheDao {

    @Query("SELECT * FROM stream_cache WHERE assetId = :assetId")
    suspend fun getCache(assetId: String): StreamCacheEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCache(cache: StreamCacheEntity)

    @Query("UPDATE stream_cache SET cachedUrl = :url, timestamp = :timestamp WHERE assetId = :assetId")
    suspend fun updateUrl(assetId: String, url: String, timestamp: Long)

    @Query("UPDATE stream_cache SET cachedDrmKeys = :drmKeys, timestamp = :timestamp WHERE assetId = :assetId")
    suspend fun updateDrmKeys(assetId: String, drmKeys: DrmKeys, timestamp: Long)

    @Transaction
    suspend fun upsertDrmKey(contentId: String, drmKeys: DrmKeys, mediaType: MediaType) {
        val existing = getCache(contentId)
        if (existing != null) {
            updateDrmKeys(contentId, drmKeys, System.currentTimeMillis())
        } else {
            insertCache(
                StreamCacheEntity(
                    assetId = contentId,
                    cachedUrl = null,
                    cachedDrmKeys = drmKeys,
                    timestamp = System.currentTimeMillis(),
                    mediaType = mediaType.value
                )
            )
        }
    }

    @Query("DELETE FROM stream_cache WHERE assetId = :contentId")
    suspend fun deleteCache(contentId: String)

    @Query("DELETE FROM stream_cache WHERE (strftime('%s','now') * 1000 - timestamp) > :expiryTime")
    suspend fun deleteExpiredCache(expiryTime: Long)

}