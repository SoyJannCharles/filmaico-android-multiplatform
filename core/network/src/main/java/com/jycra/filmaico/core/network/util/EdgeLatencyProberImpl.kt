package com.jycra.filmaico.core.network.util

import androidx.core.net.toUri
import com.jycra.filmaico.core.network.di.ProbeHttpClient
import com.jycra.filmaico.data.stream.util.EdgeLatencyProber
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeoutOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import java.util.concurrent.ConcurrentHashMap
import javax.inject.Inject

class EdgeLatencyProberImpl @Inject constructor(
    @ProbeHttpClient private val client: OkHttpClient
) : EdgeLatencyProber {

    private val cache = ConcurrentHashMap<String, CachedRtt>()
    private val CACHE_TTL_MS = 30_000L
    private val TOTAL_TIMEOUT_MS = 2200L

    override suspend fun isHostAlive(url: String?): Boolean {

        if (url.isNullOrBlank()) return false

        return withContext(Dispatchers.IO) {
            try {

                val request = Request.Builder()
                    .url(url)
                    .head()
                    .build()

                client.newCall(request).execute().use { response ->
                    response.isSuccessful
                }

            } catch (e: Exception) {
                false
            }
        }

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
                    val deferredPings = hostsToProbe.map { url ->
                        async {
                            val rtt = probeOnce(url)
                            latencies[url] = rtt
                            cache[url.toHostOnly()] = CachedRtt(rtt, System.currentTimeMillis())
                        }
                    }
                    deferredPings.awaitAll()
                }
            }
        }

        return candidates.sortedBy { latencies[it] ?: Long.MAX_VALUE }.also { sorted ->
            println("Radar de Nodos: ${sorted.take(3).joinToString { "$it (${latencies[it]}ms)" }}")
        }

    }

    private fun probeOnce(url: String): Long {

        val request = Request.Builder().url(url).head().build()
        val startNano = System.nanoTime()

        return try {
            client.newCall(request).execute().use { response ->

                val rttMs = (System.nanoTime() - startNano) / 1_000_000
                val code = response.code

                if (code < 400 || code == 405) {
                    rttMs
                } else {
                    Long.MAX_VALUE
                }

            }
        } catch (e: Exception) {
            Long.MAX_VALUE
        }
    }

    private fun String.toHostOnly(): String {
        return try {
            this.toUri().host?.lowercase() ?: this
        } catch (e: Exception) {
            this
        }
    }

    private data class CachedRtt(val rttMs: Long, val timestamp: Long)

}