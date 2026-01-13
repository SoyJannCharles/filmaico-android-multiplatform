package com.jycra.filmaico.data.anime.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.jycra.filmaico.data.anime.entity.AnimeCarouselEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AnimeCarouselDao {

    @Query("SELECT * FROM anime_carousels ORDER BY `order` ASC")
    fun getAnimeCarousels(): Flow<List<AnimeCarouselEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAnimeCarousels(carousels: List<AnimeCarouselEntity>)

    @Query("DELETE FROM anime_carousels")
    suspend fun clearAnimeCarousels()

    @Transaction
    suspend fun clearAndInsertAnimeCarousels(carousels: List<AnimeCarouselEntity>) {
        clearAnimeCarousels()
        insertAnimeCarousels(carousels)
    }

}