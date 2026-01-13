package com.jycra.filmaico.data.search.repository

import com.google.gson.Gson
import com.jycra.filmaico.data.anime.data.dao.AnimeDao
import com.jycra.filmaico.data.anime.mapper.toDomain
import com.jycra.filmaico.data.channel.data.dao.ChannelDao
import com.jycra.filmaico.data.channel.mapper.toDomain
import com.jycra.filmaico.data.movie.data.dao.MovieDao
import com.jycra.filmaico.data.movie.mapper.toDomain
import com.jycra.filmaico.data.serie.data.dao.SerieDao
import com.jycra.filmaico.data.serie.mapper.toDomain
import com.jycra.filmaico.domain.search.model.SearchResults
import com.jycra.filmaico.domain.search.repository.SearchRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class SearchRepositoryImpl @Inject constructor(
    private val movieDao: MovieDao,
    private val serieDao: SerieDao,
    private val channelDao: ChannelDao,
    private val animeDao: AnimeDao,
    private val gson: Gson,
) : SearchRepository {

    override fun searchContent(query: String): Flow<SearchResults> {

        if (query.isBlank()) {
            return flowOf(SearchResults())
        }

        val formattedQuery = "%${query.trim()}%"

        val movieResultsFlow = movieDao.searchMovies(formattedQuery)
        val serieResultsFlow = serieDao.searchSeries(formattedQuery)
        val channelResultsFlow = channelDao.searchChannels(formattedQuery)
        val animeResultsFlow = animeDao.searchAnimes(formattedQuery)

        return combine(
            movieResultsFlow,
            serieResultsFlow,
            channelResultsFlow,
            animeResultsFlow
        ) { movies, series, channels, animes ->

            val mappedMovies = movies.map { it.toDomain(gson) }
            val mappedSeries = series.map { it.toDomain(gson) }
            val mappedChannels = channels.map { it.toDomain(gson) }
            val mappedAnimes = animes.map { it.toDomain(gson) }

            SearchResults(
                movies = mappedMovies,
                series = mappedSeries,
                channels = mappedChannels,
                animes = mappedAnimes
            )

        }

    }

}