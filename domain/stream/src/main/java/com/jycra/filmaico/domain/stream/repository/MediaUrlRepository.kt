package com.jycra.filmaico.domain.stream.repository

import kotlinx.coroutines.flow.Flow

interface MediaUrlRepository {

    fun getAnimeUrl(url: String): Flow<String>

}