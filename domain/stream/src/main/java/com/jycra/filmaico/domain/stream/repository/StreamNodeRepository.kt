package com.jycra.filmaico.domain.stream.repository

interface StreamNodeRepository {

    suspend fun getOptimalStreamDomain(videoId: String, provider: String): String

    suspend fun reportStreamSuccess(videoId: String, domain: String)
    suspend fun reportStreamFailure(videoId: String, domain: String, reason: String)

}