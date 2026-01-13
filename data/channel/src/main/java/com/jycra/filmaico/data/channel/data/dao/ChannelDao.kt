package com.jycra.filmaico.data.channel.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.jycra.filmaico.data.channel.entity.ChannelEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ChannelDao {

    @Query("""
        SELECT * FROM channels
        WHERE name LIKE :query OR tagsJson LIKE :query
        LIMIT 20
    """)
    fun searchChannels(query: String): Flow<List<ChannelEntity>>

    @Query("SELECT * FROM channels ORDER BY `name` ASC")
    fun getChannels(): Flow<List<ChannelEntity>>

    @Query("SELECT * FROM channels WHERE tagsJson LIKE '%' || :tag || '%' ORDER BY `name` ASC")
    fun getChannelsByTag(tag: String): Flow<List<ChannelEntity>>

    @Query("SELECT * FROM channels WHERE id = :id")
    suspend fun getChannelById(id: String): ChannelEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChannels(channels: List<ChannelEntity>)

    @Query("DELETE FROM channels")
    suspend fun clearChannels()

    @Transaction
    suspend fun clearAndInsertChannels(channels: List<ChannelEntity>) {
        clearChannels()
        insertChannels(channels)
    }

}