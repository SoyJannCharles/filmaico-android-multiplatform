package com.jycra.filmaico.domain.search.usecase

import com.jycra.filmaico.domain.search.model.SearchResults
import com.jycra.filmaico.domain.search.repository.SearchRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SearchUseCase @Inject constructor(
    private val searchRepository: SearchRepository
) {

    operator fun invoke(query: String): Flow<SearchResults> =
        searchRepository.searchContent(query)

}