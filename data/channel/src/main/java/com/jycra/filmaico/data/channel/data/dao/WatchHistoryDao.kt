package com.jycra.filmaico.data.channel.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.jycra.filmaico.data.channel.entity.WatchHistoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface WatchHistoryDao {

    @Query("SELECT * FROM watch_history WHERE channelId = :channelId")
    suspend fun getById(channelId: String): WatchHistoryEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: WatchHistoryEntity)

    @Transaction
    suspend fun upsertWatchTime(channelId: String, newWatchTimeInSeconds: Long) {
        insert(
            WatchHistoryEntity(
                channelId,
                (getById(channelId)?.totalWatchTimeInSeconds ?: 0L) + newWatchTimeInSeconds,
                System.currentTimeMillis()
            )
        )
    }

    @Query("SELECT * FROM watch_history ORDER BY totalWatchTimeInSeconds DESC LIMIT 10")
    fun getTopWatchedChannels(): Flow<List<WatchHistoryEntity>>

}