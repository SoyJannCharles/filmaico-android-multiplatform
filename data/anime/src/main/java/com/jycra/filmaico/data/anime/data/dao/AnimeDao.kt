package com.jycra.filmaico.data.anime.data.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.jycra.filmaico.data.anime.entity.AnimeContentEntity
import com.jycra.filmaico.data.anime.entity.AnimeEntity
import com.jycra.filmaico.data.anime.entity.AnimeSeasonEntity
import com.jycra.filmaico.data.anime.entity.AnimeTagCrossRef
import com.jycra.filmaico.data.anime.entity.AnimeTagEntity
import com.jycra.filmaico.data.anime.relations.AnimeWithDetails
import kotlinx.coroutines.flow.Flow

@Dao
interface AnimeDao {

    @Query("""
        SELECT * FROM animes
        WHERE name LIKE :query OR synopsis LIKE :query
        LIMIT 20
    """)
    fun searchAnimes(query: String): Flow<List<AnimeEntity>>

    @Query("SELECT * FROM animes ORDER BY `name` ASC")
    fun getAnimes(): Flow<List<AnimeEntity>>

    @Transaction
    @Query("""
        SELECT * FROM animes
        INNER JOIN anime_tag_cross_ref ON animes.id = anime_tag_cross_ref.animeId
        WHERE anime_tag_cross_ref.tagName = :tag
    """)
    fun getAnimesByTag(tag: String): Flow<List<AnimeEntity>>

    @Transaction
    @Query("SELECT * FROM animes WHERE id = :id")
    fun getAnimeWithDetailById(id: String): Flow<AnimeWithDetails?>

    @Query("SELECT * FROM anime_content WHERE id = :contentId")
    suspend fun getAnimeContentById(contentId: String): AnimeContentEntity?

    @Query("UPDATE anime_content SET cachedStreamUrl = :url, cacheTimestamp = :timestamp WHERE id = :contentId")
    suspend fun updateCachedUrl(contentId: String, url: String?, timestamp: Long?)

    @Upsert
    suspend fun insertAnimes(animes: List<AnimeEntity>)

    @Upsert
    suspend fun insertAnimeTags(tags: List<AnimeTagEntity>)

    @Upsert
    suspend fun insertAnimeTagCrossRefs(crossRefs: List<AnimeTagCrossRef>)

    @Upsert
    suspend fun insertAnimeSeasons(seasons: List<AnimeSeasonEntity>)

    @Upsert
    suspend fun insertAnimeContent(content: List<AnimeContentEntity>)

    @Transaction
    suspend fun syncAnimesList(animes: List<AnimeEntity>, tags: List<AnimeTagEntity>, crossRefs: List<AnimeTagCrossRef>) {
        insertAnimes(animes)
        insertAnimeTags(tags)
        insertAnimeTagCrossRefs(crossRefs)
    }

}