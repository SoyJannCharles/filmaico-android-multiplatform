package com.jycra.filmaico.data.stream.repository

import com.jycra.filmaico.data.anime.data.dao.AnimeDao
import com.jycra.filmaico.data.movie.data.dao.MovieDao
import com.jycra.filmaico.data.serie.data.dao.SerieDao
import com.jycra.filmaico.data.stream.data.service.ScrapingService
import com.jycra.filmaico.domain.stream.repository.StreamProcessingRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StreamProcessingRepositoryImpl @Inject constructor(
    private val scrapingService: ScrapingService,
    private val animeDao: AnimeDao,
    private val serieDao: SerieDao,
    private val movieDao: MovieDao
) : StreamProcessingRepository {

    override suspend fun getWebViewScrapedUrl(pageUrl: String): Flow<String> {
        return scrapingService.extractStreamM3u8Url(pageUrl)
    }

    override suspend fun getRegexScrapedUrl(scrapeUrl: String, regex: String, headers: Map<String, String>?): String? {
        return scrapingService.extractStreamUrl(scrapeUrl, regex, headers)
    }

    override suspend fun getCachedUrl(
        contentId: String,
        contentType: String
    ): Pair<String?, Long?> {
        return when (contentType) {
            "anime" -> {
                val entity = animeDao.getAnimeContentById(contentId)
                Pair(entity?.cachedStreamUrl, entity?.cacheTimestamp)
            }
            "serie" -> {
                val entity = serieDao.getSerieContentById(contentId) // Necesitarás este método
                Pair(entity?.cachedStreamUrl, entity?.cacheTimestamp)
            }
            "movie" -> {
                val entity = movieDao.getMovieById(contentId) // Necesitarás este método
                Pair(entity?.cachedStreamUrl, entity?.cacheTimestamp)
            }
            else -> Pair(null, null)
        }
    }

    override suspend fun saveUrlToCache(
        contentId: String,
        contentType: String,
        url: String?
    ) {
        val timestamp = if (url != null) System.currentTimeMillis() else null
        when (contentType) {
            "anime" -> animeDao.updateCachedUrl(contentId, url, timestamp)
            "serie" -> serieDao.updateCachedUrl(contentId, url, timestamp)
            "movie" -> movieDao.updateCachedUrl(contentId, url, timestamp)
        }
    }

}