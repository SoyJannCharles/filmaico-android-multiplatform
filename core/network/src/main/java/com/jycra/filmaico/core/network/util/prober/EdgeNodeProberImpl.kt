package com.jycra.filmaico.core.network.util.prober

import com.jycra.filmaico.core.common.logger.FLog
import com.jycra.filmaico.core.common.logger.LogCategory
import com.jycra.filmaico.core.network.di.ProbeHttpClient
import com.jycra.filmaico.data.stream.util.extension.toHostOnly
import com.jycra.filmaico.data.stream.util.prober.EdgeNodeProber
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.sync.Semaphore
import kotlinx.coroutines.sync.withPermit
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeoutOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import java.util.concurrent.ConcurrentHashMap
import javax.inject.Inject
import kotlin.collections.get

class EdgeNodeProberImpl @Inject constructor(
    @ProbeHttpClient private val client: OkHttpClient
) : EdgeNodeProber {

    private data class CachedRtt(val rttMs: Long, val timestamp: Long)
    private val cache = ConcurrentHashMap<String, CachedRtt>()

    private val CACHE_TTL_MS = 30_000L
    private val TOTAL_TIMEOUT_MS = 2200L

    private val MAX_PARALLEL = 6

    override suspend fun isHostAlive(url: String?): Boolean {

        if (url.isNullOrBlank()) return false

        return probeOnce(url) != Long.MAX_VALUE

    }

    override suspend fun sortByLatency(candidates: List<String>): List<String> {

        if (candidates.size <= 1) return candidates

        val now = System.currentTimeMillis()

        val latencies = ConcurrentHashMap<String, Long>()
        val hostsToProbe = mutableListOf<String>()

        candidates.forEach { url ->

            val host = url.toHostOnly()

            val cached = cache[host]
            if (cached != null && (now - cached.timestamp) < CACHE_TTL_MS) {
                latencies[url] = cached.rttMs
            } else {
                hostsToProbe.add(url)
            }

        }

        if (hostsToProbe.isNotEmpty()) {
            withContext(Dispatchers.IO) {
                withTimeoutOrNull(TOTAL_TIMEOUT_MS) {
                    val semaphore = Semaphore(MAX_PARALLEL)
                    hostsToProbe.map { url ->
                        async {
                            semaphore.withPermit {
                                val rtt = probeOnce(url)
                                latencies[url] = rtt
                                url.toHostOnly()?.let { host ->
                                    cache[host] = CachedRtt(rtt, System.currentTimeMillis())
                                }
                            }
                        }
                    }.awaitAll()
                }
            }
        }

        return candidates.sortedBy { latencies[it] ?: Long.MAX_VALUE }.also { sorted ->

            val logMessage = sorted.joinToString(", ") { url ->

                val host = url.toHostOnly()
                val rtt = latencies[url]

                "$host=${if (rtt == null || rtt == Long.MAX_VALUE) "∞" else "${rtt}ms"}"

            }

            FLog.d(LogCategory.NETWORK, "Latency Radar: $logMessage")

        }

    }

    override fun getLastMeasuredRtt(url: String?): Long? {

        if (url.isNullOrBlank()) return null

        val host = url.toHostOnly() ?: return null

        return cache[host]?.let { cached ->
            val now = System.currentTimeMillis()
            if (now - cached.timestamp < CACHE_TTL_MS) {
                cached.rttMs
            } else {
                null
            }
        }

    }

    private fun probeOnce(url: String): Long {

        val request = Request.Builder()
            .url(url)
            .head()
            .build()

        val start = System.nanoTime()
        return try {
            client.newCall(request).execute().use { response ->
                val rtt = (System.nanoTime() - start) / 1_000_000
                if (response.code in 200..399 || response.code == 405) {
                    rtt
                } else {
                    Long.MAX_VALUE
                }
            }
        } catch (e: Exception) {
            Long.MAX_VALUE
        }

    }

}