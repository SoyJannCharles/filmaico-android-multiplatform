package com.jycra.filmaico.domain.movie.usecase

import com.jycra.filmaico.domain.movie.model.Movie
import com.jycra.filmaico.domain.movie.repository.MovieRepository
import javax.inject.Inject

class GetMovieByIdUseCase @Inject constructor(
    private val movieRepository: MovieRepository
) {

    suspend operator fun invoke(id: String): Movie? =
        movieRepository.getMovieById(id)

}