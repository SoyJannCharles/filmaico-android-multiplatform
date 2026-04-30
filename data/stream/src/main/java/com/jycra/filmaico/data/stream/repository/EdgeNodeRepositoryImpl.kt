package com.jycra.filmaico.data.stream.repository

import com.jycra.filmaico.data.stream.data.service.EdgeNodeService
import com.jycra.filmaico.data.stream.data.store.EdgeHostStore
import com.jycra.filmaico.data.stream.util.toStableEdgeKey
import com.jycra.filmaico.domain.stream.repository.EdgeNodeRepository
import java.util.concurrent.ConcurrentHashMap
import javax.inject.Inject
import androidx.core.net.toUri
import com.jycra.filmaico.data.stream.data.source.EdgeRouteSource
import com.jycra.filmaico.data.stream.util.EdgeLatencyProber
import kotlin.io.encoding.Base64

class EdgeNodeRepositoryImpl @Inject constructor(
    private val source: EdgeRouteSource,
    private val service: EdgeNodeService,
    private val hostStore: EdgeHostStore,
    private val prober: EdgeLatencyProber
) : EdgeNodeRepository {

    private val sessionCache = ConcurrentHashMap<String, String>()

    override suspend fun getOptimalEdgeHost(uri: String): String {

        val stableKey = uri.toStableEdgeKey()

        sessionCache[stableKey]?.let { cachedHost ->
            return cachedHost
        }

        source.getEdgeRoute(stableKey)?.let { firebaseRoute ->

            if (isTokenValid(firebaseRoute.url)) {

                if (prober.isHostAlive(firebaseRoute.url) && !firebaseRoute.url.isNullOrBlank()) {
                    return firebaseRoute.url!!
                } else {
                    reportEdgeFailure(uri, firebaseRoute.url ?: "", "Firebase route dead")
                }

            }

        }

        val originalHost = try {
            uri.toUri().host?.lowercase() ?: return uri
        } catch (e: Exception) {
            return uri
        }

        val rawCandidates = service.fetchCandidates(stableKey, hostStore.getExcludedHosts())

        if (rawCandidates.isNotEmpty()) {

            val preferredHosts = hostStore.getPreferredHosts()
            val sortedCandidates = prioritizeCandidates(rawCandidates, preferredHosts)

            val aliveCandidates = prober.sortByLatency(sortedCandidates)
            val winner = aliveCandidates.firstOrNull()

            if (winner != null) {
                sessionCache[stableKey] = winner
                return winner
            }

        }

        return originalHost

    }

    override suspend fun reportSuccessAndPublish(
        originalUri: String,
        resolvedUrl: String
    ) {

        val stableKey = originalUri.toStableEdgeKey()

        sessionCache[stableKey] = resolvedUrl

        source.saveEdgeRoute(stableKey, resolvedUrl, extractExpiration(resolvedUrl))

        val host = try {
            resolvedUrl.toUri().host ?: ""
        } catch (e: Exception) {
            ""
        }

        if (host.isNotEmpty()) {
            hostStore.recordSuccess(host)
        }

    }

    override suspend fun reportEdgeFailure(
        originalUri: String,
        failedHost: String,
        reason: String
    ) {

        val stableKey = originalUri.toStableEdgeKey()

        sessionCache.remove(stableKey)

        hostStore.recordFailure(failedHost)

        source.reportEdgeFailure(stableKey, failedHost, reason)

    }

    private fun isTokenValid(url: String?): Boolean {

        if (url.isNullOrBlank()) return false

        return try {

            val currentTimeSeconds = System.currentTimeMillis() / 1000

            extractExpiration(url) > (currentTimeSeconds + 300)

        } catch (e: Exception) {
            false
        }

    }

    private fun extractExpiration(url: String): Long {
        return try {

            val jwtPart = url.substringAfter("/tok_", "")
                .substringBefore("/", "")
                .split(".")
                .getOrNull(1) ?: return 0L

            val decodedBytes = Base64.decode(jwtPart, android.util.Base64.DEFAULT)
            val payloadString = String(decodedBytes)

            val pattern = Regex("\"exp\"\\s*:\\s*\"?(\\d+)\"?")
            val match = pattern.find(payloadString)

            val exp = match?.groupValues?.get(1)?.toLongOrNull() ?: 0L

            exp

        } catch (e: Exception) {
            0L
        }
    }

    private fun prioritizeCandidates(
        networkList: List<String>,
        preferred: List<String>
    ): List<String> {

        if (preferred.isEmpty()) return networkList

        val preferredAvailable = networkList.filter { candidate ->
            val host = try {
                candidate.toUri().host?.lowercase() ?: candidate
            } catch (e: Exception) {
                candidate
            }
            preferred.contains(host)
        }

        val others = networkList.filterNot { preferredAvailable.contains(it) }

        return preferredAvailable + others

    }

}