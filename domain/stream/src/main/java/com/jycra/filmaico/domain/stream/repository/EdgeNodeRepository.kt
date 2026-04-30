package com.jycra.filmaico.domain.stream.repository

interface EdgeNodeRepository {

    suspend fun getOptimalEdgeHost(uri: String): String

    suspend fun reportSuccessAndPublish(originalUri: String, resolvedUrl: String)
    suspend fun reportEdgeFailure(originalUri: String, failedHost: String, reason: String)

}