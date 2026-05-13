package com.jycra.filmaico.domain.stream.resolver

import com.jycra.filmaico.domain.stream.model.ResolutionSession

interface BaseStreamResolver {

    suspend fun resolve(url: String): ResolutionSession?
    suspend fun resolveWithWebView(url: String): ResolutionSession?

    suspend fun reportSeedSuccess(session: ResolutionSession)
    suspend fun reportSeedFailure(session: ResolutionSession)

}