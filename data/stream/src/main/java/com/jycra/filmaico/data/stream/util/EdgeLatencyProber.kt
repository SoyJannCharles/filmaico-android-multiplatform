package com.jycra.filmaico.data.stream.util

interface EdgeLatencyProber {

    suspend fun sortByLatency(candidates: List<String>): List<String>

}