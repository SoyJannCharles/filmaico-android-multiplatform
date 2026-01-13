package com.jycra.filmaico.domain.search.repository

import com.jycra.filmaico.domain.search.model.SearchResults
import kotlinx.coroutines.flow.Flow

interface SearchRepository {

    fun searchContent(query: String): Flow<SearchResults>

}