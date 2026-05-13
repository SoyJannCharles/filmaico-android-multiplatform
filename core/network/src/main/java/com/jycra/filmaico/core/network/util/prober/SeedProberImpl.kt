package com.jycra.filmaico.core.network.util.prober

import com.jycra.filmaico.core.common.logger.FLog
import com.jycra.filmaico.core.common.logger.LogCategory
import com.jycra.filmaico.core.network.di.ProbeHttpClient
import com.jycra.filmaico.data.stream.util.prober.SeedProber
import com.jycra.filmaico.data.stream.util.extension.toHostOnly
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeoutOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import javax.inject.Inject

class SeedProberImpl @Inject constructor(
    @ProbeHttpClient private val client: OkHttpClient
) : SeedProber {

    private val MAX_PARALLEL_PROBES = 4
    private val TOTAL_TIMEOUT = 3000L

    override suspend fun findWinningSeed(urls: List<String>): SeedProber.ProbeResult? = withContext(Dispatchers.IO) {

        if (urls.isEmpty()) return@withContext null

        val channel = Channel<SeedProber.ProbeResult>(Channel.CONFLATED)
        val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

        val jobs = urls.take(MAX_PARALLEL_PROBES).map { url ->
            scope.launch {
                val result = probeDeep(url)
                if (result != null) {
                    channel.send(result)
                }
            }
        }

        val winner = withTimeoutOrNull(TOTAL_TIMEOUT) {
            channel.receive()
        }

        jobs.forEach { it.cancel() }

        winner?.also {
            FLog.d(LogCategory.NETWORK, "Deep Probe Winner: ${it.url.toHostOnly()} (${it.latency}ms)")
        }

    }

    private fun probeDeep(url: String): SeedProber.ProbeResult? {

        val request = Request.Builder()
            .url(url)
            .get()
            .build()

        val start = System.nanoTime()

        return try {
            client.newCall(request).execute().use { response ->

                val rtt = (System.nanoTime() - start) / 1_000_000

                if (response.isSuccessful) {
                    val html = response.body.string()
                    if (html.contains("eval") || html.contains("p2p")) {
                        SeedProber.ProbeResult(url, html, rtt)
                    } else null
                } else null

            }
        } catch (e: Exception) {
            null
        }

    }

}