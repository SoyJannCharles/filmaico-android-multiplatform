package com.jycra.filmaico.data.stream.data.source

import com.jycra.filmaico.core.firebase.model.stream.SeedDto

interface StreamDataSource {

    suspend fun getSeedsByProvider(provider: String): List<SeedDto>
    suspend fun saveSeed(domain: String, provider: String)
    suspend fun deleteSeed(domain: String, provider: String)

}