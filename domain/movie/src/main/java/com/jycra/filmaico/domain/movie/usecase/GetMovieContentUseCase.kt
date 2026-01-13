package com.jycra.filmaico.domain.movie.usecase

import com.jycra.filmaico.domain.movie.model.MovieCarousel
import com.jycra.filmaico.domain.movie.repository.MovieRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetMovieContentUseCase @Inject constructor(
    private val movieRepository: MovieRepository
) {

    operator fun invoke(): Flow<List<MovieCarousel>> {
        return movieRepository.getMovieCarousels()
    }

}