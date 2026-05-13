package com.jycra.filmaico.domain.stream.resolver

import com.jycra.filmaico.domain.stream.model.ResolutionParams
import com.jycra.filmaico.domain.stream.model.ResolutionSession
import com.jycra.filmaico.domain.stream.util.StreamExtractionState

interface FlowStreamResolver {

    suspend fun resolve(
        params: ResolutionParams,
        lastSession: ResolutionSession? = null,
        onStateChange: (StreamExtractionState) -> Unit = {}
    ): ResolutionSession?

}