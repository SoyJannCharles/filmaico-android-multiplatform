package com.jycra.filmaico.data.stream.data.source

import com.jycra.filmaico.core.model.stream.EdgeRouteDto

interface EdgeRouteSource {

    suspend fun getEdgeRoute(stableKey: String): EdgeRouteDto?
    suspend fun saveEdgeRoute(stableKey: String, resolvedUrl: String, expiration: Long)

    suspend fun reportEdgeFailure(stableKey: String, failedHost: String, reason: String)

}