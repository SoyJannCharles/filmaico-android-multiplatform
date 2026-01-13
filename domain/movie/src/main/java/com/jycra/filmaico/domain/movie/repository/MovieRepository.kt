package com.jycra.filmaico.domain.movie.repository

import com.jycra.filmaico.domain.movie.model.Movie
import com.jycra.filmaico.domain.movie.model.MovieCarousel
import kotlinx.coroutines.flow.Flow

interface MovieRepository {

    fun getMovieCarousels(): Flow<List<MovieCarousel>>

    suspend fun getMovieById(id: String): Movie?

}