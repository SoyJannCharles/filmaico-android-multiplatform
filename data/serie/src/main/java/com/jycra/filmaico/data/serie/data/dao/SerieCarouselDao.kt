package com.jycra.filmaico.data.serie.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.jycra.filmaico.data.serie.entity.SerieCarouselEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SerieCarouselDao {

    @Query("SELECT * FROM serie_carousels ORDER BY `order` ASC")
    fun getSerieCarousels(): Flow<List<SerieCarouselEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSerieCarousels(carousels: List<SerieCarouselEntity>)

    @Query("DELETE FROM serie_carousels")
    suspend fun clearSerieCarousels()

    @Transaction
    suspend fun clearAndInsertSerieCarousels(carousels: List<SerieCarouselEntity>) {
        clearSerieCarousels()
        insertSerieCarousels(carousels)
    }

}