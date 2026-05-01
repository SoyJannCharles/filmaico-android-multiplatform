package com.jycra.filmaico.data.stream.data.source

import com.jycra.filmaico.core.firebase.model.stream.StreamRouteDto

interface StreamRouteSource {

    suspend fun getStreamDomains(provider: String): List<StreamRouteDto>

    suspend fun saveStreamDomain(provider: String, domain: String)
    suspend fun markDomainDead(provider: String, domain: String)

}