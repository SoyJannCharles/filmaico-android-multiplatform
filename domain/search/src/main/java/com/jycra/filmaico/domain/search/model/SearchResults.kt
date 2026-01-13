package com.jycra.filmaico.domain.search.model

import com.jycra.filmaico.domain.anime.model.Anime
import com.jycra.filmaico.domain.channel.model.Channel
import com.jycra.filmaico.domain.movie.model.Movie
import com.jycra.filmaico.domain.serie.model.Serie

data class SearchResults(
    val movies: List<Movie> = emptyList(),
    val series: List<Serie> = emptyList(),
    val channels: List<Channel> = emptyList(),
    val animes: List<Anime> = emptyList()
)