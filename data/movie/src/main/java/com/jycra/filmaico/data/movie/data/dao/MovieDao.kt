package com.jycra.filmaico.data.movie.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.jycra.filmaico.data.movie.entity.MovieEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieDao {

    @Query("""
        SELECT * FROM movies
        WHERE name LIKE :query OR synopsis LIKE :query
        LIMIT 20
    """)
    fun searchMovies(query: String): Flow<List<MovieEntity>>

    @Query("SELECT * FROM movies")
    fun getMovies(): Flow<List<MovieEntity>>

    @Query("SELECT * FROM movies WHERE tagsJson LIKE '%' || :tag || '%' ORDER BY `name` ASC")
    fun getMoviesByTag(tag: String): Flow<List<MovieEntity>>

    @Query("SELECT * FROM movies WHERE id = :id")
    suspend fun getMovieById(id: String): MovieEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovies(movies: List<MovieEntity>)

    @Query("UPDATE movies SET cachedStreamUrl = :url, cacheTimestamp = :timestamp WHERE id = :contentId")
    suspend fun updateCachedUrl(contentId: String, url: String?, timestamp: Long?)

    @Query("DELETE FROM movies")
    suspend fun clearMovies()

    @Transaction
    suspend fun clearAndInsertMovies(movies: List<MovieEntity>) {
        clearMovies()
        insertMovies(movies)
    }

}