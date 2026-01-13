package com.jycra.filmaico.data.movie.repository

import com.google.gson.Gson
import com.jycra.filmaico.data.movie.data.dao.MovieCarouselDao
import com.jycra.filmaico.data.movie.data.dao.MovieDao
import com.jycra.filmaico.data.movie.data.service.MovieService
import com.jycra.filmaico.data.movie.entity.MovieCarouselEntity
import com.jycra.filmaico.data.movie.entity.MovieEntity
import com.jycra.filmaico.data.movie.mapper.toDomain
import com.jycra.filmaico.data.movie.mapper.toEntity
import com.jycra.filmaico.domain.movie.model.Movie
import com.jycra.filmaico.domain.movie.model.MovieCarousel
import com.jycra.filmaico.domain.movie.repository.MovieRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.collections.map

class MovieRepositoryImpl @Inject constructor(
    private val movieService: MovieService,
    private val movieCarouselDao: MovieCarouselDao,
    private val movieDao: MovieDao,
    private val gson: Gson,
    ioDispatcher: CoroutineDispatcher
) : MovieRepository {

    private val scope = CoroutineScope(ioDispatcher + SupervisorJob())

    init {
        syncMovies()
        syncMovieCarousels()
    }

    private fun syncMovies() {
        movieService.getMoviesRealtime { remoteMoviesDto ->
            scope.launch {
                movieDao.clearAndInsertMovies(
                    remoteMoviesDto.map { movieDto ->
                        movieDto.toEntity(gson)
                    }
                )
            }
        }
    }

    private fun syncMovieCarousels() {
        movieService.getMovieCarouselsRealtime { remoteMovieCarouselsDto ->
            scope.launch {
                movieCarouselDao.clearAndInsertMovieCarousels(
                    remoteMovieCarouselsDto.map { movieCarouselDto ->
                        movieCarouselDto.toEntity(gson)
                    }
                )
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getMovieCarousels(): Flow<List<MovieCarousel>> {
        return movieCarouselDao.getMovieCarousels().flatMapLatest { movieCarouselEntities ->
            if (movieCarouselEntities.isEmpty())
                return@flatMapLatest flowOf(emptyList())
            val carouselFlows = movieCarouselEntities.map { movieCarouselEntity ->
                fetchMoviesForCarousel(movieCarouselEntity)
            }
            combine(carouselFlows) {
                    movieCarousels -> movieCarousels.toList()
            }
        }
    }

    override suspend fun getMovieById(id: String): Movie? =
        movieDao.getMovieById(id)?.toDomain(gson)

    private fun fetchMoviesForCarousel(movieCarouselEntity: MovieCarouselEntity): Flow<MovieCarousel> {

        val moviesFlow: Flow<List<MovieEntity>> = when (movieCarouselEntity.queryType) {
            "tag" -> {
                movieDao.getMoviesByTag(
                    gson.fromJson(movieCarouselEntity.queryValueJson, String::class.java)
                )
            }
            else -> flowOf(emptyList())
        }

        return moviesFlow.map { movieEntities ->
            movieCarouselEntity.toDomain(gson, movieEntities)
        }

    }

}