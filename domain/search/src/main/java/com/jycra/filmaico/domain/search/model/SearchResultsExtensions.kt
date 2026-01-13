package com.jycra.filmaico.domain.search.model

val SearchResults.totalResults: Int
    get() {
        return channels.size + movies.size + series.size + animes.size
    }

val SearchResults.firstResultIndex: Int?
    get() {
        if (channels.isNotEmpty()) {
            return 0
        }
        if (movies.isNotEmpty()) {
            return 1
        }
        if (series.isNotEmpty()) {
            return 2
        }
        if (animes.isNotEmpty()) {
            return 3
        }
        return null
    }