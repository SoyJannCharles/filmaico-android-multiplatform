package com.jycra.filmaico.data.stream.util.prober

interface EdgeNodeProber {

    suspend fun sortByLatency(candidates: List<String>): List<String>
    fun getLastMeasuredRtt(url: String?): Long?

    suspend fun isHostAlive(url: String?): Boolean

}