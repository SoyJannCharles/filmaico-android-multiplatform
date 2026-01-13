package com.jycra.filmaico.data.channel.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.jycra.filmaico.data.channel.entity.ChannelCarouselEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ChannelCarouselDao {

    @Query("SELECT * FROM channel_carousels ORDER BY `order` ASC")
    fun getChannelCarousels(): Flow<List<ChannelCarouselEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChannelCarousels(carousels: List<ChannelCarouselEntity>)

    @Query("DELETE FROM channel_carousels")
    suspend fun clearChannelCarousels()

    @Transaction
    suspend fun clearAndInsertChannelCarousels(carousels: List<ChannelCarouselEntity>) {
        clearChannelCarousels()
        insertChannelCarousels(carousels)
    }

}