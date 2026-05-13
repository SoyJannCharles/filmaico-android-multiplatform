package com.jycra.filmaico.domain.stream.repository

import com.jycra.filmaico.domain.stream.model.PlaybackParams
import com.jycra.filmaico.domain.stream.model.ResolutionParams
import com.jycra.filmaico.domain.stream.model.ResolutionResult
import com.jycra.filmaico.domain.stream.util.MediaType
import com.jycra.filmaico.domain.stream.model.ResolutionSession
import com.jycra.filmaico.domain.stream.util.StreamExtractionState
import kotlinx.coroutines.flow.Flow

interface StreamRepository {

    suspend fun resolveStream(
        resolutionParams: ResolutionParams,
        lastSession: ResolutionSession? = null,
        onStateChange: (StreamExtractionState) -> Unit = {}
    ): Flow<ResolutionResult>

    suspend fun reportSuccess(session: ResolutionSession)
    suspend fun reportFailure(session: ResolutionSession, reason: String)

    suspend fun cacheStreamUrl(assetId: String, mediaType: MediaType, url: String?)
    suspend fun getCachedStreamUrl(assetId: String, mediaType: MediaType): Pair<String?, Long?>

}