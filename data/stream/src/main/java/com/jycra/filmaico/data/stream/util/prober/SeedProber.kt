package com.jycra.filmaico.data.stream.util.prober

interface SeedProber {

    suspend fun findWinningSeed(urls: List<String>): ProbeResult?

    data class ProbeResult(
        val url: String,
        val body: String,
        val latency: Long
    )

}