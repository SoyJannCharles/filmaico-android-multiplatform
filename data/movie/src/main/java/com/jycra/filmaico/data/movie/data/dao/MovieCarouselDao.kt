package com.jycra.filmaico.data.movie.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.jycra.filmaico.data.movie.entity.MovieCarouselEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieCarouselDao {

    @Query("SELECT * FROM movie_carousels ORDER BY `order` ASC")
    fun getMovieCarousels(): Flow<List<MovieCarouselEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovieCarousels(carousels: List<MovieCarouselEntity>)

    @Query("DELETE FROM movie_carousels")
    suspend fun clearMovieCarousels()

    @Transaction
    suspend fun clearAndInsertMovieCarousels(carousels: List<MovieCarouselEntity>) {
        clearMovieCarousels()
        insertMovieCarousels(carousels)
    }

}