package com.jycra.filmaico.data.stream.resolver

import android.util.LruCache
import com.jycra.filmaico.core.common.logger.FLog
import com.jycra.filmaico.core.common.logger.LogCategory
import com.jycra.filmaico.data.stream.data.provider.EdgeNodeProvider
import com.jycra.filmaico.data.stream.util.FlowToken
import com.jycra.filmaico.data.stream.util.extension.toHostOnly
import com.jycra.filmaico.data.stream.util.prober.EdgeNodeProber
import com.jycra.filmaico.data.stream.util.toStableEdgeKey
import com.jycra.filmaico.domain.stream.model.ResolutionParams
import com.jycra.filmaico.domain.stream.model.Stream
import com.jycra.filmaico.domain.stream.model.ResolutionSession
import com.jycra.filmaico.domain.stream.resolver.FlowStreamResolver
import com.jycra.filmaico.domain.stream.util.StreamExtractionState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DefaultFlowStreamResolver @Inject constructor(
    private val provider: EdgeNodeProvider,
    private val prober: EdgeNodeProber
) : FlowStreamResolver {

    private val sessionCache = LruCache<String, ResolutionSession>(20)

    override suspend fun resolve(
        params: ResolutionParams,
        lastSession: ResolutionSession?,
        onStateChange: (StreamExtractionState) -> Unit
    ): ResolutionSession? = withContext(Dispatchers.IO) {

        val source = params.source as Stream.Direct
        val uri = source.uri
        val stableKey = uri.toStableEdgeKey()

        sessionCache.get(stableKey)?.let { session ->
            if (FlowToken.isValid(session.resolvedUrl)) {
                FLog.d(LogCategory.DATABASE, "Lvl 1 HIT: Using session cache for $stableKey")
                return@withContext session
            } else {
                sessionCache.remove(stableKey)
            }
        }

        val preferredHost = if (!params.forceRefresh) lastSession?.host else null

        FLog.d(LogCategory.API, "Lvl 1 Miss. Querying remote provider for $stableKey")
        val remoteHosts = provider.fetchCandidates(stableKey, preferredHost)
        if (remoteHosts.isNotEmpty()) {

            val sorted = prober.sortByLatency(remoteHosts)
            val best = sorted.firstOrNull()

            if (best != null) {
                FLog.d(LogCategory.API, "Lvl 2 HIT: Remote provider host selected")
                return@withContext newSession(uri, best)
            }

        }

        return@withContext null

    }

    private fun newSession(url: String, edgeUrl: String): ResolutionSession {

        val stableKey = url.toStableEdgeKey()
        val host = edgeUrl.toHostOnly() ?: ""

        val session = ResolutionSession(
            streamUrl = url,
            host = host,
            resolvedUrl = edgeUrl,
        )

        sessionCache.put(stableKey, session)
        return session

    }

}