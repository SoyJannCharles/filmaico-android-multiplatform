package com.jycra.filmaico.data.serie.data.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.jycra.filmaico.data.serie.entity.SerieContentEntity
import com.jycra.filmaico.data.serie.entity.SerieEntity
import com.jycra.filmaico.data.serie.entity.SerieSeasonEntity
import com.jycra.filmaico.data.serie.entity.SerieTagCrossRef
import com.jycra.filmaico.data.serie.entity.SerieTagEntity
import com.jycra.filmaico.data.serie.relations.SerieWithDetails
import kotlinx.coroutines.flow.Flow

@Dao
interface SerieDao {

    @Query("""
        SELECT * FROM series
        WHERE name LIKE :query OR synopsis LIKE :query
        LIMIT 20
    """)
    fun searchSeries(query: String): Flow<List<SerieEntity>>

    @Query("SELECT * FROM series ORDER BY `name` ASC")
    fun getSeries(): Flow<List<SerieEntity>>

    @Transaction
    @Query(
        """
        SELECT * FROM series
        INNER JOIN serie_tag_cross_ref ON series.id = serie_tag_cross_ref.serieId
        WHERE serie_tag_cross_ref.tagName = :tag
    """
    )
    fun getSeriesByTag(tag: String): Flow<List<SerieEntity>>

    @Transaction
    @Query("SELECT * FROM series WHERE id = :id")
    fun getSerieWithDetailsById(id: String): Flow<SerieWithDetails?>

    @Query("SELECT * FROM serie_content WHERE id = :contentId")
    suspend fun getSerieContentById(contentId: String): SerieContentEntity?

    @Query("UPDATE serie_content SET cachedStreamUrl = :url, cacheTimestamp = :timestamp WHERE id = :contentId")
    suspend fun updateCachedUrl(contentId: String, url: String?, timestamp: Long?)

    @Upsert
    suspend fun insertSeries(series: List<SerieEntity>)

    @Upsert
    suspend fun insertSerieTags(tags: List<SerieTagEntity>)

    @Upsert
    suspend fun insertSerieTagCrossRefs(crossRefs: List<SerieTagCrossRef>)

    @Upsert
    suspend fun insertSerieSeasons(seasons: List<SerieSeasonEntity>)

    @Upsert
    suspend fun insertSerieContent(content: List<SerieContentEntity>)

    @Transaction
    suspend fun syncSeriesList(series: List<SerieEntity>, tags: List<SerieTagEntity>, crossRefs: List<SerieTagCrossRef>) {
        insertSeries(series)
        insertSerieTags(tags)
        insertSerieTagCrossRefs(crossRefs)
    }

}